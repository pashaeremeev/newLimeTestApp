package com.example.new_practice.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.new_practice.ClickQualityListener
import com.example.new_practice.Quality
import com.example.new_practice.R
import com.example.new_practice.adapters.QualityAdapter

@UnstableApi
class ItemQualityFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.quality_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.qualityList)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val qualities: ArrayList<Quality> =
            if (arguments != null) arguments?.getSerializable(
                LIST_BUNDLE_KEY
            ) as ArrayList<Quality> else ArrayList()
        recyclerView.adapter = QualityAdapter(
            view.context,
            qualities,
            object : ClickQualityListener {
                override fun invoke(quality: Quality?) {
                    clickOnQuality(quality!!)
                }
            }
        )
        return view
    }

    private fun clickOnQuality(quality: Quality) {
        val result = Bundle()
        result.putInt(BUNDLE_KEY, quality.index)
        requireActivity().supportFragmentManager.setFragmentResult(REQUEST_KEY, result)
        dismiss()
        isExist = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        isExist = false
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setGravity(Gravity.END or Gravity.BOTTOM)
        window.setBackgroundDrawable(
            resources.getDrawable(R.drawable.bg_quality, activity?.theme)
        )
        val params = window.attributes
        params.x = 55
        params.y = 100
        window.attributes = params
    }

    companion object {
        const val BUNDLE_KEY = "qualityDialogKey"
        const val REQUEST_KEY = "qualityRequestKey"
        const val LIST_BUNDLE_KEY = "LIST_BUNDLE_KEY"
        var isExist = false
        fun getInstance(list: ArrayList<Quality>, current: Int): ItemQualityFragment {
            val itemQualityFragment = ItemQualityFragment()
            list[current].isCurrent = 1
            val bundle = Bundle()
            bundle.putSerializable(LIST_BUNDLE_KEY, list)
            itemQualityFragment.arguments = bundle
            isExist = true
            return itemQualityFragment
        }
    }
}