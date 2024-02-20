package com.example.designapp.database

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designapp.R
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import java.io.InputStream
import java.util.UUID

@Composable
fun RealTimeDatabase() {
    val contex = LocalContext.current
    val database = Firebase.database
    val myRef = database.getReference("Models")


    var selectedFile: InputStream? by remember {
        mutableStateOf(null)
    }
    var ModelName by remember {
        mutableStateOf("")
    }
    var ModelCategorey by remember {
        mutableStateOf("")
    }
    var ModelImage by remember {
        mutableStateOf("")
    }
//    val img: Bitmap = BitmapFactory.decodeResource(Resources.getSystem(),android.R.drawable.ic_menu_report_image)
//    val bitmap = remember {
//        mutableStateOf(img)
//    }
//    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContract.TakePicturePreview(),0)
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Text(text = "Hi",
            fontSize = 16.sp,
            color = Color.Red
            )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(value = ModelName, onValueChange = {ModelName=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text(text = "Enter Model Name", fontSize = 14.sp)
            }

        )

        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(value = ModelCategorey, onValueChange = {ModelCategorey=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text(text = "Enter Model Categorey", fontSize = 14.sp)
            }

        )
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { selectedUri ->
                    // Handle the selected file URI
                    val contentResolver = contex.contentResolver
                    selectedFile = contentResolver.openInputStream(selectedUri)
                }
            }



        Spacer(modifier = Modifier.padding(10.dp))
        Row {
            OutlinedButton(
                onClick = {
                    if (ModelName.isNotEmpty() && ModelCategorey.isNotEmpty() && selectedFile != null) {
                        val storage = Firebase.storage
                        val storageRef =
                            storage.reference.child("uploads").child("${UUID.randomUUID()}")
                        val uploadTask = storageRef.putStream(selectedFile!!)

                        uploadTask.addOnSuccessListener { taskSnapshot ->
                            // File uploaded successfully, get the download URL
                            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                // Now you can use the download URL to store it in your database or perform other actions
                                val downloadUrl = downloadUri.toString()

                                // Save downloadUrl along with ModelName and ModelCategorey to Firebase Realtime Database
                                val modelInfo = ModelInfo(ModelName, ModelCategorey, downloadUrl)
                                myRef.child(ModelName).setValue(modelInfo).addOnSuccessListener {
                                    ModelName = ""
                                    ModelCategorey = ""
                                    Toast.makeText(contex, "Successfully Added", Toast.LENGTH_SHORT)
                                        .show()
                                }.addOnFailureListener {
                                    Toast.makeText(contex, it.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                contex,
                                "File upload failed: ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

//                                val ModelInfo = ModelInfo(ModelName, ModelCategorey)
//                                myRef.child(ModelName).setValue(ModelInfo).addOnSuccessListener {
//                                    ModelName = ""
//                                    ModelCategorey = ""
//                                    Toast.makeText(contex, "Successfully Added", Toast.LENGTH_SHORT)
//                                        .show()
//                                }.addOnFailureListener {
//                                    Toast.makeText(contex, it.toString(), Toast.LENGTH_SHORT)
//                                }
//                            }
                    else {
                        Toast.makeText(
                            contex,
                            "Please enter value first",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.text_medium),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .offset(y = -5.dp)
            ) {
                Text(text = "Insert", fontSize = 16.sp, color = Color.White)
            }

        }
    }
}


