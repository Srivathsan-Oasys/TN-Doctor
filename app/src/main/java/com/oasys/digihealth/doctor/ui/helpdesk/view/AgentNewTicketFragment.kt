package com.oasys.digihealth.doctor.ui.helpdesk.view


import android.os.Bundle
import android.view.*

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.oasys.digihealth.doctor.R
import com.oasys.digihealth.doctor.config.AppConstants
import com.oasys.digihealth.doctor.config.AppPreferences
import com.oasys.digihealth.doctor.databinding.ActivityAgentTicketBinding
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.AgentTicketViewModel
import com.oasys.digihealth.doctor.ui.helpdesk.viewmodel.AgentTicketViewModelFactory
import com.oasys.digihealth.doctor.db.UserDetailsRoomRepository
import com.oasys.digihealth.doctor.utils.Utils


class AgentNewTicketFragment : Fragment() {
    var binding: ActivityAgentTicketBinding? = null
    private var viewModel: AgentTicketViewModel? = null
    private var utils: Utils? = null
    var appPreferences: AppPreferences? = null
    var userDetailsRoomRepository: UserDetailsRoomRepository? = null
    private var facility_id: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.activity_agent_ticket,
                container,
                false
            )


        viewModel = AgentTicketViewModelFactory(
            requireActivity().application
        )
            .create(AgentTicketViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        utils = Utils(requireContext())

        appPreferences = AppPreferences.getInstance(
            requireActivity().application,
            AppConstants.SHARE_PREFERENCE_NAME
        )
        userDetailsRoomRepository = UserDetailsRoomRepository(requireActivity().application)
        val userDataStoreBean = userDetailsRoomRepository.getUserDetails()

        facility_id = appPreferences?.getInt(AppConstants.FACILITY_UUID)!!

        setupViewPager(binding.viewpager!!)
        binding.viewpager!!.setOffscreenPageLimit(2)
        binding.tabs!!.setupWithViewPager(binding.viewpager)


        return binding!!.root
    }

    private fun setupViewPager(viewPager: ViewPager) {

        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(PublicFragment(), "Public")
        adapter.addFragment(UserFragment(), "User")
        adapter.addFragment(VendorFragment(), "Vendor")

        viewPager.adapter = adapter

    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
        FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = java.util.ArrayList<Fragment>()
        private val mFragmentTitleList = java.util.ArrayList<String>()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }

}