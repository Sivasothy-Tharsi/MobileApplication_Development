package com.example.designapp.categorey

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.designapp.Camera
import com.example.designapp.Login.LoginViewModel
import com.example.designapp.R
import com.example.designapp.database.ModelInfo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.storage
import java.io.InputStream
import java.util.UUID


@SuppressLint("StateFlowValueCalledInComposition")

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun FloorDesign(navController: NavHostController, viewModel: LoginViewModel = hiltViewModel()) {
    val contex = LocalContext.current

    var selectedCategory by remember { mutableStateOf("Rangoli")
    }


    val state by viewModel.loginState.collectAsState(initial = null)

    var userEmail by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))


    ) {

        // Header
        Row(
            modifier = Modifier

                .fillMaxWidth()
                .background(color = colorResource(id = R.color.text_medium))
                .padding(16.dp)
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Floor Designs",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.weight(1f)
            )
//            Text(text = data.toString())

            var isModalVisible by remember { mutableStateOf(false) }
            // Fetch the current user's email when the component is recomposed
            LaunchedEffect(viewModel) {
                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser

                if(currentUser != null) {
                    userEmail = currentUser.email.toString()
                }
            }
//            val isButtonVisible by floorDesignVM.isButtonVisible.collectAsState()

            if (userEmail=="mmadhunicka7@gmail.com" ) {
                OutlinedButton(
                    onClick = {
                        isModalVisible = true
                    }
                ) {
                    Icon(

                        imageVector = Icons.Default.Add,
                        contentDescription = null, // Content description for accessibility
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)

                    )
                }
                if (isModalVisible) {
                    PopupModal(onDismiss = {
                        isModalVisible = false
                    })
                }
            }




            // Show the modal when the button is clicked

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            CategoryTextButton("Rangoli", selectedCategory == "Rangoli") {
                selectedCategory = "Rangoli"
            }
            Spacer(modifier = Modifier.width(16.dp))
            CategoryTextButton("Kolam", selectedCategory == "Kolam") {
                selectedCategory = "Kolam"
            }
            Spacer(modifier = Modifier.width(16.dp))
            CategoryTextButton("Carpet", selectedCategory == "Carpet") {
                selectedCategory = "Carpet"
            }

        }

        // Bottom part of the screen
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {


            when (selectedCategory) {
                "Rangoli" -> RangoliScreen(navController)
                "Kolam" -> KolamScreen()
                "Carpet" -> CarpetScreen()
            }
        }
    }


}

@Composable
fun CategoryTextButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (isSelected) colorResource(id = R.color.text_medium) else MaterialTheme.colorScheme.onBackground
        )



    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RangoliScreen(navController: NavHostController) {
    val contex = LocalContext.current
    val storage = Firebase.storage
    val storageRef = storage.reference.child("uploads") // Replace with your storage path
    val context = LocalContext.current

    // State to hold the list of file names
    var fileNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var fileImages by remember { mutableStateOf<List<String>>(emptyList()) }

    var models by remember {
        mutableStateOf<List<ModelInfo>>(emptyList())
    }

    val database = Firebase.database
    val myRef6 = database.getReference("Models")
    // Effect to fetch file names when the composable is first drawn
    var modelInfoList by remember { mutableStateOf<List<ModelInfo>>(emptyList()) }
    fun addModelInfoItem(newItem: ModelInfo) {
        modelInfoList = modelInfoList + newItem
    }
    LaunchedEffect(Unit) {
        myRef6.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                modelInfoList = emptyList()
                val dataList = mutableListOf<String>()
                val dataList2 = mutableListOf<String>()

                for (childSnapshot in snapshot.children) {
                    // Extract the 'name' field from each child

                    val name = childSnapshot.child("modelName").getValue(String::class.java)
                    name?.let { dataList.add(it) }
                    val thumb = childSnapshot.child("downloadUrl").getValue(String::class.java)
                    thumb?.let { dataList2.add(it) }

                    Log.d("asd", childSnapshot.value.toString());


                    val modelName = childSnapshot.child("modelName").getValue(String::class.java)
                    val modelCategory =
                        childSnapshot.child("modelCategorey").getValue(String::class.java)
                    val downloadUrl =
                        childSnapshot.child("downloadUrl").getValue(String::class.java)

                    val modelInfo = ModelInfo(modelName, modelCategory, downloadUrl)
//                    modelInfoList.add(modelInfo)
                    addModelInfoItem(modelInfo)


                }

                // Update the state with the list of names
                fileNames = dataList
                fileImages = dataList2

                // Log the data changes
                Log.d("RangoliScreen", "Data updated: $fileNames")

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.e("RangoliScreen", "Error fetching data: ${error.message}")
            }
        })

    }
    Column(
        modifier = Modifier


            .size(1600.dp)
            .verticalScroll(rememberScrollState())

    )
    {

//        Text("Rangoli Screen Content")

        Column(
            modifier = Modifier
//                .fillMaxSize()
                .fillMaxWidth()


        ) {
            val modelsInFirstColumn = modelInfoList.take(modelInfoList.size / 2)
            val modelsInSecondColumn = modelInfoList.drop(modelInfoList.size / 2)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxSize()

                ) {
                    modelsInFirstColumn.forEach { modelInfo ->

                        if(modelInfo.ModelCategorey=="Rangoli") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()

                                    .padding(8.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        val intent = Intent(context, Camera::class.java)
                                        intent.putExtra("ModelName", modelInfo.ModelName)
                                        intent.putExtra("DownloadUrl", modelInfo.downloadUrl)
                                        context.startActivity(intent)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)

                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.nobggry),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)

                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                color = Color.Gray,
                                            )
                                    )

                                    Text(
                                        text = " ${modelInfo.ModelName}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    modelsInSecondColumn.forEach { modelInfo ->
                        if(modelInfo.ModelCategorey=="Rangoli") {
                            Box(
                                modifier = Modifier

                                    .padding(8.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    .clickable {
                                        val intent = Intent(context, Camera::class.java)
                                        intent.putExtra("ModelName", modelInfo.ModelName)
                                        intent.putExtra("DownloadUrl", modelInfo.downloadUrl)
                                        context.startActivity(intent)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.nobggry),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)

                                            .clip(RoundedCornerShape(8.dp))
                                            .background(color = Color.Gray)
                                    )

                                    Text(
                                        text = " ${modelInfo.ModelName}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }



}




//}

@Composable
fun FileItem(fileName: String) {
    // Modify this function to display each file item as needed
    // You can use the fileName to create a clickable item or any other UI representation
    Text(text = fileName, modifier = Modifier.clickable {
        // Handle item click (e.g., open file, perform action, etc.)
    })
}

@Composable
fun KolamScreen() {
    val contex = LocalContext.current
    val storage = Firebase.storage
    val storageRef = storage.reference.child("uploads") // Replace with your storage path
    val context = LocalContext.current

    // State to hold the list of file names
    var fileNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var fileImages by remember { mutableStateOf<List<String>>(emptyList()) }

    var models by remember {
        mutableStateOf<List<ModelInfo>>(emptyList())
    }

    val database = Firebase.database
    val myRef = database.getReference("Models")
    // Effect to fetch file names when the composable is first drawn
    var modelInfoList by remember { mutableStateOf<List<ModelInfo>>(emptyList()) }
    fun addModelInfoItem(newItem: ModelInfo) {
        modelInfoList = modelInfoList + newItem
    }
    LaunchedEffect(Unit) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                modelInfoList = emptyList()
                val dataList = mutableListOf<String>()
                val dataList2 = mutableListOf<String>()

                for (childSnapshot in snapshot.children) {
                    // Extract the 'name' field from each child

                    val name = childSnapshot.child("modelName").getValue(String::class.java)
                    name?.let { dataList.add(it) }
                    val thumb = childSnapshot.child("downloadUrl").getValue(String::class.java)
                    thumb?.let { dataList2.add(it) }

                    Log.d("asd", childSnapshot.value.toString());


                    val modelName = childSnapshot.child("modelName").getValue(String::class.java)
                    val modelCategory =
                        childSnapshot.child("modelCategorey").getValue(String::class.java)
                    val downloadUrl =
                        childSnapshot.child("downloadUrl").getValue(String::class.java)

                    val modelInfo = ModelInfo(modelName, modelCategory, downloadUrl)
//                    modelInfoList.add(modelInfo)
                    addModelInfoItem(modelInfo)


                }

                // Update the state with the list of names
                fileNames = dataList
                fileImages = dataList2

                // Log the data changes
                Log.d("RangoliScreen", "Data updated: $fileNames")

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.e("RangoliScreen", "Error fetching data: ${error.message}")
            }
        })

    }
    Column(
        modifier = Modifier


            .size(1600.dp)
            .verticalScroll(rememberScrollState())

    )
    {

//        Text("Kolam Screen Content")


        Column(
            modifier = Modifier
//                .fillMaxSize()
                .fillMaxWidth()
                .padding(0.dp)

        ) {
            val modelsInFirstColumn = modelInfoList.take(modelInfoList.size / 2)
            val modelsInSecondColumn = modelInfoList.drop(modelInfoList.size / 2)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxSize()

                ) {

                    modelsInFirstColumn.forEach { modelInfo ->
                        if(modelInfo.ModelCategorey=="Kolam") {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()

                                    .padding(8.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        val intent = Intent(context, Camera::class.java)
                                        intent.putExtra("ModelName", modelInfo.ModelName)
                                        intent.putExtra("DownloadUrl", modelInfo.downloadUrl)
                                        context.startActivity(intent)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)

                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.nobggry),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)

                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                color = Color.Gray,
                                            )
                                    )

                                    Text(
                                        text = " ${modelInfo.ModelName}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    modelsInSecondColumn.forEach { modelInfo ->
                        if(modelInfo.ModelCategorey=="Kolam") {
                            Box(
                                modifier = Modifier

                                    .padding(8.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    .clickable {
                                        val intent = Intent(context, Camera::class.java)
                                        intent.putExtra("ModelName", modelInfo.ModelName)
                                        intent.putExtra("DownloadUrl", modelInfo.downloadUrl)
                                        context.startActivity(intent)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.nobggry),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)

                                            .clip(RoundedCornerShape(8.dp))
                                            .background(color = Color.Gray)
                                    )

                                    Text(
                                        text = " ${modelInfo.ModelName}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }







}

@Composable
fun CarpetScreen() {
    val contex = LocalContext.current
    val storage = Firebase.storage
    val storageRef = storage.reference.child("uploads") // Replace with your storage path
    val context = LocalContext.current

    // State to hold the list of file names
    var fileNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var fileImages by remember { mutableStateOf<List<String>>(emptyList()) }

    var models by remember {
        mutableStateOf<List<ModelInfo>>(emptyList())
    }

    val database = Firebase.database
    val myRef3 = database.getReference("Models")
    // Effect to fetch file names when the composable is first drawn
    var modelInfoList by remember { mutableStateOf<List<ModelInfo>>(emptyList()) }
    fun addModelInfoItem(newItem: ModelInfo) {
        modelInfoList = modelInfoList + newItem
    }
    LaunchedEffect(Unit) {
        myRef3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                modelInfoList = emptyList()
                val dataList = mutableListOf<String>()
                val dataList2 = mutableListOf<String>()

                for (childSnapshot in snapshot.children) {
                    // Extract the 'name' field from each child

                    val name = childSnapshot.child("modelName").getValue(String::class.java)
                    name?.let { dataList.add(it) }
                    val thumb = childSnapshot.child("downloadUrl").getValue(String::class.java)
                    thumb?.let { dataList2.add(it) }

                    Log.d("asd", childSnapshot.value.toString());


                    val modelName = childSnapshot.child("modelName").getValue(String::class.java)
                    val modelCategory =
                        childSnapshot.child("modelCategorey").getValue(String::class.java)
                    val downloadUrl =
                        childSnapshot.child("downloadUrl").getValue(String::class.java)

                    val modelInfo = ModelInfo(modelName, modelCategory, downloadUrl)
//                    modelInfoList.add(modelInfo)
                    addModelInfoItem(modelInfo)


                }

                // Update the state with the list of names
                fileNames = dataList
                fileImages = dataList2

                // Log the data changes
                Log.d("RangoliScreen", "Data updated: $fileNames")

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.e("RangoliScreen", "Error fetching data: ${error.message}")
            }
        })

    }
    Column(
        modifier = Modifier
            .size(1600.dp)
            .verticalScroll(rememberScrollState())

    )
    {

//        Text("Carpet Screen Content")


        Column(
            modifier = Modifier
//                .fillMaxSize()
                .fillMaxWidth()
                .padding(0.dp)

        ) {
            val modelsInFirstColumn = modelInfoList.take(modelInfoList.size / 2)
            val modelsInSecondColumn = modelInfoList.drop(modelInfoList.size / 2)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxSize()

                ) {

                    modelsInFirstColumn.forEach { modelInfo ->
                        if(modelInfo.ModelCategorey=="Carpet") {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()

                                    .padding(8.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        val intent = Intent(context, Camera::class.java)
                                        intent.putExtra("ModelName", modelInfo.ModelName)
                                        intent.putExtra("DownloadUrl", modelInfo.downloadUrl)
                                        context.startActivity(intent)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)

                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.nobggry),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)

                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                color = Color.Gray,
                                            )
                                    )

                                    Text(
                                        text = " ${modelInfo.ModelName}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(4.dp)

                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    modelsInSecondColumn.forEach { modelInfo ->
                        if(modelInfo.ModelCategorey=="Carpet") {
                            Box(
                                modifier = Modifier

                                    .padding(8.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )

                                    .clickable {
                                        val intent = Intent(context, Camera::class.java)
                                        intent.putExtra("ModelName", modelInfo.ModelName)
                                        intent.putExtra("DownloadUrl", modelInfo.downloadUrl)
                                        context.startActivity(intent)
                                    }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.nobggry),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp)

                                            .clip(RoundedCornerShape(8.dp))
                                            .background(color = Color.Gray)
                                    )

                                    Text(
                                        text = " ${modelInfo.ModelName}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }






}


@Composable
fun rememberLauncher(onResult: (Uri?) -> Unit): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        onResult(uri)
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PopupModal(onDismiss: () -> Unit) {

    var selectedFile: InputStream? by remember {
        mutableStateOf(null)
    }

    var isUploading by remember {
        mutableStateOf(false)
    }

    val contex = LocalContext.current

    val database = Firebase.database
    val myRef2 = database.getReference("Models")
    var ModelName by remember {
        mutableStateOf("")
    }
    var ModelCategorey by remember {
        mutableStateOf("")
    }
    var ModelImage by remember {
        mutableStateOf("")
    }
    var categoryOptions by remember {
        mutableStateOf(
            listOf("Rangoli", "Kolam", "Carpet")
        )
    }
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),


        ) {

        Surface(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(size = 16.dp),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .offset(y = 15.dp, x = 100.dp)

                        .background(

                            color = Color.Gray,
                            shape = CircleShape,

                            )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }


                Spacer(modifier = Modifier.padding(10.dp))
                androidx.compose.material.OutlinedTextField(value = ModelName,
                    onValueChange = { ModelName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)


                       ,
                    placeholder = {
                        Text(text = "Enter Model Name", fontSize = 14.sp)
                    },
                    shape = RoundedCornerShape(8.dp)

                )




                Spacer(modifier = Modifier.padding(10.dp))


//                androidx.compose.material.OutlinedTextField(value = ModelCategorey,
//                    onValueChange = { ModelCategorey = it },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp),
//                    placeholder = {
//                        Text(text = "Enter Model Categorey", fontSize = 14.sp)
//                    }
//
//                )
                Row(
                    modifier = Modifier

                        .background(MaterialTheme.colorScheme.surface)

                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                        .clickable { expanded = !expanded }
                        .padding(10.dp)
                        .width(250.dp)


                ) {
                    BasicTextField(
                        value = categoryOptions[selectedCategoryIndex],
                        onValueChange = {},
                        readOnly = true,

                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .weight(2f)


                            .clickable { expanded = !expanded }

                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,

                        modifier = Modifier
                            .size(24.dp)
                            .clickable { expanded = !expanded }
                    )
                }

                if (expanded) {
                    Dialog(onDismissRequest = { expanded = false }) {
                        Column {
                            categoryOptions.forEachIndexed { index, option ->
                                Text(
                                    text = option,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            selectedCategoryIndex = index
                                            expanded = false
                                        }
                                )
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.padding(10.dp))

                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                        uri?.let { selectedUri ->
                            // Handle the selected file URI
                            val contentResolver = contex.contentResolver
                            selectedFile = contentResolver.openInputStream(selectedUri)
                        }
                    }

                Button(
                    onClick = {
                        // Launch the file picker
                        launcher.launch("*/*")
                    },
                    modifier = Modifier.padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White

                )
                )

                {
                    Text(text = "Select File")
                }



                Row {
                    OutlinedButton(
                        onClick = {
                            if (ModelName.isNotEmpty()  && selectedFile != null && ModelName.length > 0 && !isUploading) {
                                isUploading = true;
                                val storage = Firebase.storage
                                val storageRef =
                                    storage.reference.child("uploads").child("${UUID.randomUUID()}.glb")
                                val metadata = StorageMetadata.Builder().setContentType("application/octet-stream").build()
                                val uploadTask = storageRef.putStream(selectedFile!!,metadata)
                                val selectedModelCategory = categoryOptions[selectedCategoryIndex]

                                uploadTask.addOnSuccessListener { taskSnapshot ->
                                    // File uploaded successfully, get the download URL
                                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                        // Now you can use the download URL to store it in your database or perform other actions
                                        val downloadUrl = downloadUri.toString()

                                        // Save downloadUrl along with ModelName and ModelCategorey to Firebase Realtime Database
                                        val modelInfo =
                                            ModelInfo(ModelName, selectedModelCategory, downloadUrl)

                                        val uniqueKey = myRef2.push().key

                                        myRef2.child(ModelName).setValue(modelInfo).addOnSuccessListener {
//
                                            Toast.makeText(contex, "Successfully Added", Toast.LENGTH_SHORT).show()
                                            isUploading = false;
                                        }.addOnFailureListener {
                                            Toast.makeText(contex, it.toString(), Toast.LENGTH_SHORT).show()
                                            isUploading = false;
                                        }
                                        ModelName = ""
////                                            ModelCategorey = ""
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(contex, "File upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                                    isUploading = false;
                                }
                            }

                            else {
                                var text = "Please enter value first";
                                var uploading = "Uploading in progress. Please wait.."
                                if(isUploading){
                                    text = uploading;
                                }


                                Toast.makeText(
                                    contex,
                                    text,
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
    }
}