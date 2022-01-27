package ru.mironov.persons_cards_list_viewpager.ui

import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.android.synthetic.main.part_result.view.*
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.viewmodel.Status

object ResultRenderer {
    fun renderResult(status: Status, partResult: LinearLayout) {
        when (status) {

            is Status.DATA -> {
                partResult.visibility= View.GONE
            }
            is Status.LOADING -> {
                partResult.visibility = View.GONE
            }
            is Status.ERROR -> {
                partResult.visibility= View.VISIBLE
                partResult.resultTextTop.visibility= View.VISIBLE
                partResult.resultTextMiddle.visibility= View.VISIBLE
                partResult.resultTextBottom.visibility= View.VISIBLE
                partResult.resultImage.visibility= View.VISIBLE
                partResult.resultImage.setImageDrawable(AppCompatResources.getDrawable(partResult.context,R.drawable.ufo))
                partResult.resultTextTop.setText(R.string.someone_broke_everything)
                partResult.resultTextMiddle.setText(R.string.we_will_fix)
                partResult.resultTextBottom.setText(R.string.try_again)
            }
            is Status.EMPTY -> {
                partResult.visibility= View.VISIBLE
                partResult.resultImage.visibility= View.VISIBLE
                partResult.resultTextTop.visibility= View.VISIBLE
                partResult.resultTextMiddle.visibility= View.VISIBLE
                partResult.resultTextBottom.visibility= View.VISIBLE
                partResult.resultImage.setImageDrawable(AppCompatResources.getDrawable(partResult.context,R.drawable.search_glass))
                partResult.resultTextTop.setText(R.string.nobody_found)
                partResult.resultTextMiddle.setText(R.string.correct_your_search)
                partResult.resultTextBottom.setText(R.string.empty)
            }
        }
    }
}