package ca.burchill.cointracker.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ca.burchill.cointracker.R
import ca.burchill.cointracker.databinding.FragmentCoinListBinding
import ca.burchill.cointracker.viewModels.CoinApiStatus
import ca.burchill.cointracker.viewModels.CoinListViewModel


class CoinListFragment : Fragment() {

    private val viewModel: CoinListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, CoinListViewModel.Factory(activity.application)).get(
            CoinListViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCoinListBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        val adapter = CoinListAdapter()
        binding.coinList.adapter = adapter

        viewModel.coins.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Observer for the status.
        viewModel.status.observe(viewLifecycleOwner, Observer<CoinApiStatus> { status ->
            when(status){
                CoinApiStatus.LOADING -> toastNetworkEvent("Loading Data", Toast.LENGTH_SHORT)
                CoinApiStatus.ERROR -> toastNetworkEvent("Network Error", Toast.LENGTH_LONG)
                CoinApiStatus.DONE -> toastNetworkEvent("Data Loaded", Toast.LENGTH_SHORT)
            }
        })

        return binding.root
    }

    /**
     * Method for displaying a Toast message for network activity.
     */
    private fun toastNetworkEvent(msg: String, duration: Int) {
        Toast.makeText(activity, msg, duration).show()
    }
}