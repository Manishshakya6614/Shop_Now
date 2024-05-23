package com.manish.shopnow.dialog

import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.manish.shopnow.R

// I created extension function because the ame dialog is used in multiple fragments, so we don't need to rewrite this builder again and again
fun Fragment.setupBottomSheetDialog(
    onSendClick: (String) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
    val view = layoutInflater.inflate(R.layout.reset_password_bottom_sheet_dialog, null)
    dialog.setContentView(view)
    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    dialog.show()

    val etEmail = view.findViewById<EditText>(R.id.etEmailResetPassword)
    val btnCancel = view.findViewById<Button>(R.id.btnCancelResetPassword)
    val btnSend = view.findViewById<Button>(R.id.btnSendResetPassword)

    btnSend.setOnClickListener {
        val email = etEmail.text.toString().trim()
        onSendClick(email)
        dialog.dismiss()
    }
    btnCancel.setOnClickListener {
        dialog.dismiss()
    }
}