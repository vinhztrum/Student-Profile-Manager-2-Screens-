package com.thanhvinh.studentprofilemanager

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.thanhvinh.studentprofilemanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Đặt biến TAG vào trong companion object theo chuẩn[cite: 5]
    companion object {
        private const val TAG = "TAG_LIFECYCLE"
    }

    private lateinit var binding: ActivityMainBinding
    private var currentStudent = Student("2415053122248", "PHAM TRAN THANH VINH", "24T2", "vinh@ute.udn.vn", 3.8)

    // 1. Launcher xử lý trả về dữ liệu (Edit Profile)[cite: 14, 26]
    private val editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedStudent = result.data?.getSerializableExtra("UPDATED_STUDENT") as? Student
            updatedStudent?.let {
                currentStudent = it
                bindStudentData(currentStudent)
                Toast.makeText(this, "Đã lưu thông tin mới của ${it.name}!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 2. Launcher mở Gallery lấy ảnh (GetContent)[cite: 17, 26]
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.imgAvatar.setImageURI(it)
            Toast.makeText(this, "Đã đổi ảnh đại diện thành công!", Toast.LENGTH_SHORT).show()
        }
    }

    // 3. Launcher xin quyền Camera (RequestPermission)[cite: 20, 26]
    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Đã cấp quyền Camera! Có thể chụp ảnh ngay.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bạn đã từ chối quyền Camera. Tính năng này bị khóa!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: Activity đang được khởi tạo và nạp layout")

        bindStudentData(currentStudent)

        // Nút 1: Mở màn hình Edit[cite: 8, 26]
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java).apply {
                putExtra("EXTRA_STUDENT_OBJECT", currentStudent)
            }
            editLauncher.launch(intent)
        }

        // Nút 2: Mở Photo Picker[cite: 17, 26]
        binding.btnChangeAvatar.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        // Nút 3: Gọi cố vấn học tập[cite: 11, 24]
        binding.btnCallHotline.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:0905123456")
            }
            try {
                startActivity(Intent.createChooser(dialIntent, "Chọn ứng dụng gọi điện"))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Không tìm thấy ứng dụng gọi điện!", Toast.LENGTH_SHORT).show()
            }
        }

        // Nút 4: Yêu cầu quyền Camera[cite: 20, 26]
        binding.btnRequestCamera.setOnClickListener {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun bindStudentData(student: Student) {
        binding.tvName.text = student.name
        binding.tvDetails.text = "Mã SV: ${student.id} | Lớp: ${student.className}\nEmail: ${student.email}"
        binding.tvGpaBadge.text = "GPA: ${student.gpa}"
    }

    // --- Override Lifecycle Callbacks[cite: 5] ---
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity đã hiển thị trên màn hình")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity ở trạng thái đỉnh stack, sẵn sàng tương tác!")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity bị che khuất một phần")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity bị ẩn hoàn toàn")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Người dùng mở lại Activity từ trạng thái Stop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity bị hủy hoàn toàn khỏi bộ nhớ RAM!")
    }
}