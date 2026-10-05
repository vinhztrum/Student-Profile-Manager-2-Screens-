package com.thanhvinh.studentprofilemanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thanhvinh.studentprofilemanager.databinding.ActivityEditProfileBinding // Thay bằng package name của bạn

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var originalStudent: Student? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu cũ[cite: 9, 28]
        originalStudent = intent.getSerializableExtra("EXTRA_STUDENT_OBJECT") as? Student

        originalStudent?.let {
            binding.edtName.setText(it.name)
            binding.edtClass.setText(it.className)
            binding.edtGpa.setText(it.gpa.toString())
        }

        // Xử lý nút Lưu[cite: 15, 28]
        binding.btnSave.setOnClickListener {
            val newName = binding.edtName.text.toString().trim()
            val newClass = binding.edtClass.text.toString().trim()
            val newGpa = binding.edtGpa.text.toString().toDoubleOrNull()

            if (newName.isEmpty() || newClass.isEmpty() || newGpa == null || newGpa !in 0.0..4.0) {
                Toast.makeText(this, "Vui lòng nhập tên, lớp và GPA hợp lệ (0.0-4.0)!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedStudent = originalStudent?.copy(name = newName, className = newClass, gpa = newGpa) ?: return@setOnClickListener

            val resultIntent = Intent().apply {
                putExtra("UPDATED_STUDENT", updatedStudent)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Xử lý nút Hủy Bỏ (trả về RESULT_CANCELED ngầm)[cite: 15, 28]
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
}