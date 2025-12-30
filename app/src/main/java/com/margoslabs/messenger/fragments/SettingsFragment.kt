package com.margoslabs.messenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.margoslabs.messenger.databinding.FragmentSettingsBinding
import com.margoslabs.messenger.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val TAG = "SettingsFragment"
    
    // Получаем ViewModel через делегат viewModels()
    private val viewModel: SettingsViewModel by viewModels()
    
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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Представление Fragment создано")
        
        setupObservers()
        setupThemeSwitch()
    }
    
    /**
     * Настройка наблюдателей LiveData для реактивного обновления UI
     */
    private fun setupObservers() {
        // Наблюдаем за изменениями темы
        viewModel.isDarkTheme.observe(viewLifecycleOwner) { isDark ->
            Log.d(TAG, "isDarkTheme observer: Получена новая тема - темная: $isDark")
            // Обновляем переключатель только если значение изменилось
            if (binding.switchTheme.isChecked != isDark) {
                binding.switchTheme.isChecked = isDark
            }
            // Применяем тему
            applyTheme(isDark)
        }
    }
    
    /**
     * Настройка переключателя темы
     */
    private fun setupThemeSwitch() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Переключатель темы изменен: $isChecked")
            // Обновляем ViewModel
            viewModel.setDarkTheme(isChecked)
        }
    }
    
    /**
     * Применить тему приложения
     */
    private fun applyTheme(isDark: Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
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

