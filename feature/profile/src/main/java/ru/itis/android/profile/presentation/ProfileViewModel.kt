package ru.itis.android.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.android.data.datastore.SessionManager
import ru.itis.android.data.session.CurrentUserHolder
import ru.itis.android.database.dao.UserDao
import ru.itis.android.database.entity.UserEntity
import ru.itis.android.model.Service
import ru.itis.android.model.UserRole
import ru.itis.android.repository.MasterRepository
import ru.itis.android.repository.ReviewsRepository
import ru.itis.android.repository.ServicesRepository
import ru.itis.android.usecase.CreateServiceUseCase
import ru.itis.android.usecase.GetCategoriesUseCase
import ru.itis.android.usecase.GetMyServicesUseCase
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getMyServicesUseCase: GetMyServicesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createServiceUseCase: CreateServiceUseCase,
    private val servicesRepository: ServicesRepository,
    private val masterRepository: MasterRepository,
    private val reviewsRepository: ReviewsRepository,
    private val sessionManager: SessionManager,
    private val userDao: UserDao,
    private val currentUserHolder: CurrentUserHolder
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileScreenState())
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    private val _createServiceState =
        MutableStateFlow<CreateServiceState>(CreateServiceState.Idle())
    val createServiceState: StateFlow<CreateServiceState> = _createServiceState.asStateFlow()

    init {
        observeUser()
        loadCategories()
    }

    private fun observeUser() {
        viewModelScope.launch {
            currentUserHolder.user.collect { user ->
                if (user == null) {
                    _state.update {
                        ProfileScreenState(
                            isLoading = false,
                            categories = it.categories
                        )
                    }
                    _createServiceState.value = CreateServiceState.Idle()
                } else {
                    applyUser(user)
                    if (UserRole.fromRaw(user.role) == UserRole.MASTER) {
                        loadMyServices()
                        loadReviewsForMaster(user.id)
                    } else {
                        _state.update { it.copy(myServices = emptyList(), masterReviews = emptyList()) }
                    }
                }
            }
        }
    }

    private fun applyUser(user: UserEntity) {
        _state.update {
            it.copy(
                role = UserRole.fromRaw(user.role),
                fullName = user.fullName,
                phone = user.phone,
                email = user.email,
                about = user.about,
                experienceYears = user.experienceYears,
                isLoading = false
            )
        }
    }

    private fun loadMyServices() {
        viewModelScope.launch {
            getMyServicesUseCase()
                .onSuccess { mine ->
                    _state.update { it.copy(myServices = mine, errorMessage = null) }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(errorMessage = error.message ?: "Не удалось загрузить услуги")
                    }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().onSuccess { categories ->
                _state.update { it.copy(categories = categories) }
            }
        }
    }

    fun refresh() {
        loadCategories()
        if (_state.value.role == UserRole.MASTER) loadMyServices()
    }

    fun refreshOwnData() {
        val user = currentUserHolder.user.value ?: return
        if (UserRole.fromRaw(user.role) == UserRole.MASTER) {
            loadMyServices()
            loadReviewsForMaster(user.id)
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }


    private var lastRequestedMasterId: String? = null
    private var masterProfileJob: Job? = null
    private var masterReviewsJob: Job? = null

    fun loadMasterProfile(masterId: String) {
        masterProfileJob?.cancel()
        masterReviewsJob?.cancel()
        lastRequestedMasterId = masterId
        _state.update {
            it.copy(
                masterProfileLoading = true,
                selectedMasterProfile = null,
                masterProfileError = null,
                masterReviews = emptyList(),
                masterReviewsLoading = true
            )
        }
        masterProfileJob = viewModelScope.launch {
            masterRepository.getMasterById(masterId)
                .onSuccess { profile ->
                    _state.update {
                        it.copy(
                            selectedMasterProfile = profile,
                            masterProfileLoading = false,
                            masterProfileError = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            masterProfileLoading = false,
                            masterProfileError = error.message ?: "Не удалось загрузить профиль"
                        )
                    }
                }
        }
        masterReviewsJob = loadReviewsForMaster(masterId)
    }

    private fun loadReviewsForMaster(masterId: String): Job {
        _state.update { it.copy(masterReviewsLoading = true) }
        return viewModelScope.launch {
            reviewsRepository.getReviewsForMaster(masterId)
                .onSuccess { reviews ->
                    _state.update {
                        it.copy(masterReviews = reviews, masterReviewsLoading = false)
                    }
                }
                .onFailure {
                    _state.update { it.copy(masterReviewsLoading = false) }
                }
        }
    }

    fun retryLoadMasterProfile() {
        lastRequestedMasterId?.let { loadMasterProfile(it) }
    }

    fun clearMasterProfile() {
        masterProfileJob?.cancel()
        masterReviewsJob?.cancel()
        masterProfileJob = null
        masterReviewsJob = null
        lastRequestedMasterId = null
        _state.update {
            it.copy(
                selectedMasterProfile = null,
                masterProfileLoading = false,
                masterProfileError = null,
                masterReviews = emptyList(),
                masterReviewsLoading = false
            )
        }
    }


    fun updateCreateTitle(value: String) = updateCreate { it.copy(title = value) }
    fun updateCreateDescription(value: String) = updateCreate { it.copy(description = value) }
    fun updateCreatePrice(value: String) =
        updateCreate { it.copy(price = value.filter(Char::isDigit)) }

    fun updateCreateCategory(categoryId: Long) =
        updateCreate { it.copy(categoryId = categoryId) }

    fun resetCreateService() {
        _createServiceState.value = CreateServiceState.Idle()
    }

    fun startCreateService() {
        _createServiceState.value = CreateServiceState.Idle()
    }

    fun startEditService(service: Service) {
        _createServiceState.value = CreateServiceState.Idle(
            editingServiceId = service.id,
            title = service.title,
            description = service.description.orEmpty(),
            price = service.price.toString(),
            categoryId = service.categoryId
        )
    }

    fun submitCreateService() {
        val current = _createServiceState.value as? CreateServiceState.Idle ?: return

        if (current.title.isBlank()) {
            _createServiceState.value = current.copy(errorMessage = "Введите название")
            return
        }
        if (current.description.isBlank()) {
            _createServiceState.value = current.copy(errorMessage = "Введите описание")
            return
        }
        val price = current.price.toIntOrNull()
        if (price == null || price <= 0) {
            _createServiceState.value = current.copy(errorMessage = "Введите корректную цену")
            return
        }
        val categoryId = current.categoryId
        if (categoryId == null) {
            _createServiceState.value = current.copy(errorMessage = "Выберите категорию")
            return
        }

        _createServiceState.value = current.copy(isSubmitting = true, errorMessage = null)
        val editingId = current.editingServiceId

        viewModelScope.launch {
            val result = if (editingId != null) {
                servicesRepository.updateService(
                    id = editingId,
                    title = current.title.trim(),
                    description = current.description.trim(),
                    price = price,
                    categoryId = categoryId
                )
            } else {
                createServiceUseCase(
                    title = current.title.trim(),
                    description = current.description.trim(),
                    price = price,
                    categoryId = categoryId
                )
            }

            result.onSuccess { saved ->
                _state.update { state ->
                    val updatedList = if (editingId != null) {
                        state.myServices.map { if (it.id == saved.id) saved else it }
                    } else {
                        listOf(saved) + state.myServices
                    }
                    state.copy(myServices = updatedList)
                }
                _createServiceState.value = CreateServiceState.Success
            }.onFailure { error ->
                _createServiceState.value = current.copy(
                    isSubmitting = false,
                    errorMessage = error.message ?: "Не удалось сохранить услугу"
                )
            }
        }
    }

    fun deleteService(serviceId: Long) {
        viewModelScope.launch {
            servicesRepository.deleteService(serviceId)
                .onSuccess {
                    _state.update { it.copy(myServices = it.myServices.filterNot { s -> s.id == serviceId }) }
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message ?: "Не удалось удалить услугу") }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearToken()
            userDao.clear()
        }
    }

    private inline fun updateCreate(block: (CreateServiceState.Idle) -> CreateServiceState.Idle) {
        val current = _createServiceState.value as? CreateServiceState.Idle ?: return
        _createServiceState.value = block(current).copy(errorMessage = null)
    }
}
