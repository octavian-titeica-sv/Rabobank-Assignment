package com.example.csvreader.ui.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.csvreader.R
import com.example.csvreader.domain.model.UserModel
import java.util.Locale
import kotlinx.android.synthetic.main.ui_module_user_item_view.view.user_name_text_view as userNameTextView
import kotlinx.android.synthetic.main.ui_module_user_item_view.view.issue_count_text_view as issueCountTextView
import kotlinx.android.synthetic.main.ui_module_user_item_view.view.user_date_of_birth_text_view as userDateOfBirthTextView

class UserItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    var userModel = UserModel()
        set(value) {
            field = value
            setupView()
        }

    init {
        View.inflate(context, R.layout.ui_module_user_item_view, this)
        layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setupView() {
        userNameTextView.text = String.format(Locale.getDefault(), context.getString(R.string.user_name), userModel.firstName, userModel.surName)
        issueCountTextView.text = String.format(Locale.getDefault(), context.getString(R.string.issue_count), userModel.issueCount)
        userDateOfBirthTextView.text = String.format(
            Locale.getDefault(),
            context.getString(R.string.date_of_birth),
            userModel.dateOfBirth)
    }
}
