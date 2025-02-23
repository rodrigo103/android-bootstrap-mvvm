package ar.com.wolox.android.bootstrap.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ar.com.wolox.android.bootstrap.network.util.RequestStatus

abstract class BaseFragment<V : BaseView, M : BaseViewModel<V>> : Fragment(), BaseView {

    abstract val viewModel: M

    open fun setListeners() { }

    open fun setObservers() { }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.requestStatus.observe(viewLifecycleOwner) {
            when (it) {
                RequestStatus.Loading -> showLoading()
                else -> hideLoading()
            }
        }
        setListeners()
        setObservers()
    }

    override fun showLoading() {
        (requireActivity() as BaseActivity).showLoading()
    }

    override fun hideLoading() {
        (requireActivity() as BaseActivity).hideLoading()
    }
}
