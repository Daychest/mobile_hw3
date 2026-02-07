package com.example.mobile_hw3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile_hw3.ui.theme.Mobile_hw3Theme
import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.mobile_hw3.roomDb.Note
import com.example.mobile_hw3.roomDb.NoteDatabase
import com.example.mobile_hw3.viewModel.NoteViewModel
import com.example.mobile_hw3.viewModel.Repository
import androidx.core.net.toUri

var imageStr: String = ""


class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            name = "note.db"
        ).build()
    }
    private val viewModel by viewModels<NoteViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NoteViewModel(Repository(db)) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mobile_hw3Theme {
                Navigation(viewModel, this)
            }
        }
    }
}

const val mainScreenRoute = "mainScreen"
const val messageScreenRoute = "messageScreen"
const val settingsScreenRoute = "settingsScreen"

@Composable
fun Navigation(viewModel: NoteViewModel, lifecycleOwner: LifecycleOwner) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = mainScreenRoute) {
        composable(route = mainScreenRoute) {
            MainScreen(navController = navController)
        }
        composable(route = messageScreenRoute) {
            MessageScreen(navController = navController)
        }
        composable(route = settingsScreenRoute) {
            SettingsScreen(navController = navController, viewModel, lifecycleOwner)
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            onClick = {
                navController.navigate(messageScreenRoute)
            },
        ) {
            Text(text = "Strange Man")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(settingsScreenRoute)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
        ) {
            Text(text = "Settings")
        }
    }
}


@Composable
fun MessageScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.width(190.dp),
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Text(text = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.width(190.dp),
                onClick = {
                    navController.navigate(settingsScreenRoute)
                },
                colors = ButtonDefaults.buttonColors(Color.DarkGray)
            ) {
                Text(text = "Settings")
            }
        }
        Mobile_hw3Theme() {
            Conversation(SampleData.conversationSample)
        }
    }
}

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    lifecycleOwner: LifecycleOwner
) {
    Log.d("", "SettingsScreen called")
    var noteList by remember {
        mutableStateOf(listOf<Note>())
    }
    viewModel.getNotes().observe(lifecycleOwner){
        noteList = it
    }



    val context = LocalContext.current

    var rememberedImageStr by remember {
        mutableStateOf<String>("")
    }
    if (noteList.isNotEmpty()){
        Log.d("", "Loaded from note list")
        rememberedImageStr = noteList[0].noteBody
    }



    val singleImagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            rememberedImageStr = uri.toString()
            Log.d("", "Image changed")
            context.contentResolver.takePersistableUriPermission(
                rememberedImageStr.toUri(),
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            Log.d("", "button hit")
            singleImagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = "pick image")
        }
        Button(onClick = {
            Log.d("", "str button hit")
            rememberedImageStr = "content://media/picker/0/com.android.providers.media.photopicker/media/41"


            context.contentResolver.takePersistableUriPermission(
                rememberedImageStr.toUri(),
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }) {
            Text(text = "set as string")
        }
        Button(onClick = {
            Log.d("", "save button hit")
            val note: Note = Note("TestName", rememberedImageStr, 0)
            viewModel.upsertNote(note)
        }) {
            Text(text = "save")
        }



        AsyncProfilePicture(rememberedImageStr)
        //var username by remember {  mutableStateOf("") }
        var textFieldText = ""
        TextField(
            value = textFieldText, onValueChange = { newText ->
                textFieldText = newText
            },
            label = { Text(text = "Username") })
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
        ) {
            Text(text = "Back")
        }

    }
}

data class Message(val author: String, val body: String)

@Composable
fun AsyncProfilePicture(str: String) {
    Log.d("", "AsyncImage triggered")
    AsyncImage(
        model = str.toUri(),
        contentDescription = "",
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
    )
}

@Composable
fun ProfilePicture(picture: Int) {
    Image(
        painter = painterResource(picture),
        contentDescription = null,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
    )
}

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        ProfilePicture(R.drawable.whiteness)
        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Preview
@Composable
fun Test() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var selectedImageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        val singleImagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImageUri = uri
            }
        )

        Column(Modifier.fillMaxSize()) {
            Button(onClick = {
                singleImagePickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = "pick single Image")
            }
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "",
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
