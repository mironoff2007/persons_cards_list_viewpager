package ru.mironov.persons_cards_list_viewpager.ui

import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.android.synthetic.main.part_result.view.*
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import java.util.ArrayList

object ResultRenderer {
    fun renderResult(status: Status, partResult: LinearLayout) {
        when (status) {

            is Status.DATA -> {
                partResult.visibility= View.GONE
            }
            is Status.LOADING -> {
                partResult.visibility= View.VISIBLE
                partResult.result_image.visibility= View.GONE
                partResult.result_text_top.visibility= View.GONE
                partResult.result_text_middle.visibility= View.GONE
                partResult.result_text_bottom.visibility= View.GONE
            }
            is Status.ERROR -> {
                partResult.visibility= View.VISIBLE
                partResult.result_text_top.visibility= View.VISIBLE
                partResult.result_text_middle.visibility= View.VISIBLE
                partResult.result_text_bottom.visibility= View.VISIBLE
                partResult.result_image.visibility= View.VISIBLE
                partResult.progressBar.visibility= View.GONE
                partResult.result_image.setImageDrawable(AppCompatResources.getDrawable(partResult.context,R.drawable.ufo))
                partResult.result_text_top.setText(R.string.someone_broke_everything)
                partResult.result_text_middle.setText(R.string.we_will_fix)
                partResult.result_text_bottom.setText(R.string.try_again)
            }
            is Status.EMPTY -> {
                partResult.visibility= View.VISIBLE
                partResult.result_image.visibility= View.VISIBLE
                partResult.result_text_top.visibility= View.VISIBLE
                partResult.result_text_middle.visibility= View.VISIBLE
                partResult.result_text_bottom.visibility= View.VISIBLE
                partResult.progressBar.visibility= View.GONE
                partResult.result_image.setImageDrawable(AppCompatResources.getDrawable(partResult.context,R.drawable.search_glass))
                partResult.result_text_top.setText(R.string.nobody_found)
                partResult.result_text_middle.setText(R.string.correct_your_search)
                partResult.result_text_bottom.setText(R.string.empty)
            }
        }
    }
}