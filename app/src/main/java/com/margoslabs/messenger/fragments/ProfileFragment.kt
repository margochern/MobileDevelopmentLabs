package com.margoslabs.messenger.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.margoslabs.messenger.databinding.FragmentProfileBinding
import com.margoslabs.messenger.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ProfileFragment"
    
    // Получаем ViewModel через делегат viewModels()
    private val viewModel: ProfileViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Fragment создается")
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: Создается представление Fragment")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Представление Fragment создано")
        
        setupObservers()
        setupEditTextListeners()
    }
    
    /**
     * Настройка наблюдателей LiveData для реактивного обновления UI
     */
    private fun setupObservers() {
        // Наблюдаем за изменениями имени пользователя
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            Log.d(TAG, "userName observer: Получено новое имя - '$name'")
            // Обновляем EditText только если значение изменилось (чтобы избежать бесконечного цикла)
            if (binding.etName.text.toString() != name) {
                binding.etName.setText(name)
            }
        }
        
        // Наблюдаем за изменениями статуса пользователя
        viewModel.userStatus.observe(viewLifecycleOwner) { status ->
            Log.d(TAG, "userStatus observer: Получен новый статус - '$status'")
            // Обновляем EditText только если значение изменилось
            if (binding.etStatus.text.toString() != status) {
                binding.etStatus.setText(status)
            }
        }
    }
    
    /**
     * Настройка слушателей EditText для сохранения данных в ViewModel
     */
    private fun setupEditTextListeners() {
        // Слушатель для имени пользователя
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val name = s?.toString() ?: ""
                if (name != viewModel.userName.value) {
                    viewModel.updateUserName(name)
                }
            }
        })
        
        // Слушатель для статуса пользователя
        binding.etStatus.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val status = s?.toString() ?: ""
                if (status != viewModel.userStatus.value) {
                    viewModel.updateUserStatus(status)
                }
            }
        })
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Fragment становится видимым")
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Fragment получает фокус")
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Fragment теряет фокус")
    }
    
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Fragment становится невидимым")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Представление Fragment уничтожается")
        _binding = null
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Fragment уничтожается")
    }
}

