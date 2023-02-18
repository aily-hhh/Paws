package com.hhh.paws.ui.viewPager2Gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.hhh.paws.databinding.FragmentViewPager2GalleryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewPager2GalleryFragment : Fragment() {

    private var _binding: FragmentViewPager2GalleryBinding? = null
    private val mBinding get() = _binding!!

    private var viewPager2Gallery: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPager2GalleryBinding.inflate(inflater, container, false)
        return mBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager2Gallery = mBinding.viewPager2Gallery
        val viewPager2GalleryAdapter = ViewPager2GalleryAdapter()
        viewPager2GalleryAdapter.setListViewPager(mutableListOf("1", "2", "3"))
        viewPager2Gallery?.adapter = viewPager2GalleryAdapter
        viewPager2Gallery?.setPageTransformer(ZoomOutPageTransformer())
        viewPager2Gallery?.setCurrentItem(2, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

internal class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.alpha = 0f
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                view.translationX = horzMargin - vertMargin / 2
            } else {
                view.translationX = -horzMargin + vertMargin / 2
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor

            // Fade the page relative to its size.
            view.alpha = MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA)
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.alpha = 0f
        }
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }
}