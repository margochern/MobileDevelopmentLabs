package com.margoslabs.messenger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.margoslabs.messenger.databinding.FragmentFeedBinding
import com.margoslabs.messenger.ui.adapter.MessageAdapter
import com.margoslabs.messenger.viewmodel.FeedViewModel
import kotlinx.coroutines.launch

class FeedFragment : Fragment() {
    
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val TAG = "FeedFragment"
    
    private val viewModel: FeedViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }
    private lateinit var messageAdapter: MessageAdapter
    
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
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Представление Fragment создано")
        
        setupRecyclerView()
        setupObservers()
        setupRefreshButton()
    }
    
    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(requireContext()) { messageId ->
            viewModel.toggleLike(messageId)
        }
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }
    
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Наблюдаем за сообщениями
                viewModel.messages.collect { messages ->
                    Log.d(TAG, "Messages updated: ${messages.size}")
                    messageAdapter.submitList(messages)
                    updateEmptyState(messages.isEmpty())
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Наблюдаем за состоянием загрузки
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Наблюдаем за ошибками
                viewModel.error.collect { error ->
                    error?.let {
                        Log.e(TAG, "Error: $it")
                        // Можно показать Snackbar с ошибкой
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Наблюдаем за состоянием сети
                viewModel.isOnline.collect { isOnline ->
                    Log.d(TAG, "Network state: ${if (isOnline) "Online" else "Offline"}")
                    // Можно показать индикатор состояния сети
                }
            }
        }
    }
    
    private fun setupRefreshButton() {
        binding.btnRefresh.setOnClickListener {
            Log.d(TAG, "Refresh button clicked")
            viewModel.refreshMessages()
        }
        binding.fabRefresh.setOnClickListener {
            Log.d(TAG, "FAB refresh clicked")
            viewModel.refreshMessages()
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.tvEmptyState.visibility = if (isEmpty && !viewModel.isLoading.value) {
            View.VISIBLE
        } else {
            View.GONE
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

