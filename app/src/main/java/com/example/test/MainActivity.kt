@file:Suppress("DEPRECATION")

package com.example.test
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.theme.TestTheme
import androidx.compose.material3.Text as Text1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "mainScreen",
        modifier = modifier
    ) {
        composable("mainScreen") {
            MainScreen(
                onNavigateToSecondScreen = {
                    navController.navigate("EnterScreen")
                }
            )
        }
        composable("EnterScreen") {
            EnterScreen(
                onBack = { navController.popBackStack() },
                onNavigateToThirdScreen = {
                    navController.navigate("ThirdScreen")
                },
                onNavigateToChatScreen = {
                    navController.navigate("ChatScreen")
                },

                onNavigateToMainScreen = {
                    navController.navigate("MainScreen")
                }
            )
        }
        composable("ThirdScreen") {
            ThirdScreen(onBack = { navController.popBackStack() },

                onNavigateToSecondScreen = {
                    navController.navigate("EnterScreen")
                }



                )
        }
        composable("ChatScreen") {
            ChatScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCallScreen = { navController.navigate("CallScreen") },
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                onNavigateToSettingsScreen = { navController.navigate("SettingsScreen") },
                onNavigateToSecondScreen = { navController.navigate("EnterScreen")},
                onNavigateToChatDetail = { chatMessage ->
                    navController.navigate("ChatDetailScreen/${chatMessage.senderName}")
                }

            )
        }
        composable("SettingsScreen") {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                onNavigateToCallScreen = { navController.navigate("CallScreen") },
                onNavigateToMessageScreen = { navController.navigate("ChatScreen") },
            )
        }


        composable("CallScreen") {
            CallScreen(
                onBack = { navController.popBackStack() },
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                onNavigateToMessageScreen = { navController.navigate("ChatScreen") },
                onNavigateToSettingsScreen = { navController.navigate("SettingsScreen") }
            )
        }


        composable("ProfileScreen") {
            ProfileScreen(
                onBack = { navController.popBackStack() },
            onNavigateToMessageScreen = { navController.navigate("ChatScreen") },
            onNavigateToSettingsScreen = { navController.navigate("SettingsScreen") },
            onNavigateToCallScreen = { navController.navigate("CallScreen") }
            )
        }

        composable("ChatDetailScreen/{senderName}") { backStackEntry ->
            val senderName = backStackEntry.arguments?.getString("senderName") ?: "Unknown"
            ChatDetailScreen(senderName = senderName, onBack = {
                navController.popBackStack()
            })
        }


    }
}

@Composable
fun MainScreen(onNavigateToSecondScreen: () -> Unit, modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize(),

    ) {
        Image(
            painter = painterResource(id = R.drawable.spirit),
            contentDescription = "Icons",
            modifier = Modifier
                .size(400.dp)
                .padding(start = 2.dp, bottom =140.dp),
            contentScale = ContentScale.Fit
        )
    }



    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        Column(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            Image(
                painter = painterResource(id = R.drawable.trio),
                contentDescription = "Icons",
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text1(
                text = "Залишайтеся в безпеці будь-де \nта будь-коли",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.security),
                    contentDescription = "Secure icon",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text1(
                    text = "Secure, private messaging",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(50.dp))

            Button(
                onClick = onNavigateToSecondScreen,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .width(300.dp)
                    .height(40.dp)

            ) {
                Text(
                    text = "Перейти до чатів",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterScreen(
    onBack: () -> Unit,
    onNavigateToThirdScreen: () -> Unit,
    onNavigateToChatScreen: () -> Unit,
    onNavigateToMainScreen: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        IconButton(
            onClick = {onNavigateToMainScreen()},
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text1(
                text = "Вхід",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text1(
                text = "Будь ласка, увійдіть у свій акаунт, \nаби продовжити",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))


            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text1("Ваш email") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))


            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text1("Ваш пароль") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onNavigateToChatScreen() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFCB45))
            ) {
                Text1(text = "Вхід", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("Не має акаунту? ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF2196F3), textDecoration = TextDecoration.Underline)) {
                        append("Зареєструватися")
                    }
                },
                style = TextStyle(fontSize = 14.sp),
                onClick = { onNavigateToThirdScreen() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 68.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TestTheme {
        MainScreen(onNavigateToSecondScreen = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    TestTheme {
        EnterScreen(
            onBack = {},
            onNavigateToThirdScreen = {},
            onNavigateToChatScreen = {},
            onNavigateToMainScreen={}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    TestTheme {
        ChatScreen(
            onBack = {},
            onNavigateToCallScreen = {},
            onNavigateToProfileScreen = {},
            onNavigateToSettingsScreen = {},
            onNavigateToSecondScreen={},
            onNavigateToChatDetail={}
        )
    }
}



@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    TestTheme {
        SettingsScreen(
            onBack = {},
            onNavigateToProfileScreen = {},
            onNavigateToCallScreen ={},
            onNavigateToMessageScreen={},
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CallScreenPreview() {
    TestTheme {
        CallScreen(
            onBack = {},
            onNavigateToProfileScreen = {},
            onNavigateToMessageScreen={},
            onNavigateToSettingsScreen = {}
        )
    }
}




@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TestTheme {
        ProfileScreen(
            onBack = {},
            onNavigateToCallScreen ={},
            onNavigateToMessageScreen={},
            onNavigateToSettingsScreen = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileThirdPreview() {
    TestTheme {
        ThirdScreen(
            onBack = {},
            onNavigateToSecondScreen={}
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(onBack: () -> Unit,onNavigateToSecondScreen: () -> Unit) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text1(
                text = "Реєстрація",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text1(
                text = "Будь ласка зареєструйтесь аби \n продовжити далі",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text1("Ваше ім'я") },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text1("Ваш email") },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text1("Ваш пароль") },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text1("Підтвердіть Ваш пароль") },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFFFFC107),
                    unfocusedIndicatorColor = Color(0xFFFFC107),
                    cursorColor = Color(0xFFFFC107)
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.run { buttonColors(containerColor = Color(0xFFFFCB45)) }
            ) {
                Text1(text = "Реєстрація", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Исправление блока ClickableText
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("Є акаунт? ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF2196F3), textDecoration = TextDecoration.Underline)) {
                        append("Війти")
                    }
                },
                style = TextStyle(fontSize = 14.sp),
                onClick = { onNavigateToSecondScreen() }
            )
        }
    }
}




@Composable
fun ChatScreen(onBack: () -> Unit, onNavigateToCallScreen: () -> Unit, onNavigateToProfileScreen:() -> Unit, onNavigateToSettingsScreen:() -> Unit, onNavigateToSecondScreen: () -> Unit, onNavigateToChatDetail: (ChatMessage) -> Unit, ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Text(
                            text = "З поверненням, Mio",
                            style = TextStyle(color = Color.White, fontSize = 24.sp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Image(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "log-out",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { }

                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        Image(
                            painter = painterResource(id = R.drawable.log),
                            contentDescription = "log-out",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onNavigateToSecondScreen() }

                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .background(Color.Yellow, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Story",
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add Story",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }


                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar7),
                                contentDescription = "Melania",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Yellow, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Melania",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }


                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar2),
                                contentDescription = "Ramona",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Yellow, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ramona",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }


                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.avatar3),
                                contentDescription = "Kate",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Yellow, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Kate",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }






            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .background(Color.White)
            ) {
                ChatScreen2(onChatClick = { chatMessage ->
                    onNavigateToChatDetail(chatMessage)
                })
            }











            BottomNavigation(
                backgroundColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {

                BottomNavigationItem(
                    selected = true,
                    onClick = {  },
                    icon = {

                        Icon(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "Messages Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Повідомлення",
                            fontSize = 10.sp
                        )
                    }
                )

                BottomNavigationItem(
                    selected = false,
                    onClick = { onNavigateToCallScreen () },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.calls),
                            contentDescription = "Calls Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Дзвінки",
                            fontSize = 12.sp
                        )
                    }
                )

                BottomNavigationItem(
                    selected = false,
                    onClick = { onNavigateToProfileScreen() },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Профіль",
                            fontSize = 12.sp
                        )
                    }
                )

                BottomNavigationItem(
                    selected = false,
                    onClick = { onNavigateToSettingsScreen() },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = "Settings Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "Налаштування",
                            fontSize = 11.sp
                        )
                    }
                )
            }

        }
    }
}


@Composable
fun CallScreen(onBack: () -> Unit,  onNavigateToProfileScreen:() -> Unit, onNavigateToSettingsScreen:() -> Unit, onNavigateToMessageScreen:() -> Unit ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Дзвінки",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.call),
                contentDescription = "Call",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )

        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally ) {
                Text(
                    text = "Останні",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(250.dp))

                Text(
                    text = "Дзвінків поки не має...",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {

            BottomNavigationItem(
                selected = true,
                onClick = { onNavigateToMessageScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.message),
                        contentDescription = "Messages Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Повідомлення",
                        fontSize = 10.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calls),
                        contentDescription = "Calls Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Дзвінки",
                        fontSize = 12.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { onNavigateToProfileScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Профіль",
                        fontSize = 12.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { onNavigateToSettingsScreen()},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Налаштування",
                        fontSize = 11.sp
                    )
                }
            )
        }


    }


}




@Composable
fun ProfileScreen(onBack: () -> Unit, onNavigateToSettingsScreen:() -> Unit, onNavigateToMessageScreen:() -> Unit, onNavigateToCallScreen: () -> Unit ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Mio",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@mio-san",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }


            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Відображуване ім'я",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mio",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Про себе",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "................",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Відображуваний телефон",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "097 98 555-0104",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }


        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }


        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            BottomNavigationItem(
                selected = true,
                onClick = { onNavigateToMessageScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.message),
                        contentDescription = "Messages Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Повідомлення",
                        fontSize = 10.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { onNavigateToCallScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calls),
                        contentDescription = "Calls Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Дзвінки",
                        fontSize = 12.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Профіль",
                        fontSize = 12.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { onNavigateToSettingsScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Налаштування",
                        fontSize = 11.sp
                    )
                }
            )
        }
    }

}



@Composable
fun SettingsScreen(onBack: () -> Unit, onNavigateToProfileScreen: () -> Unit, onNavigateToCallScreen: () -> Unit, onNavigateToMessageScreen: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Налаштування",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Mio",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 140.dp, start = 30.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.key),
                            contentDescription = "Account Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(text = "Профіль", fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black)
                            Text(text = "Змінити налаштування профілю", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.message2),
                            contentDescription = "Іконка",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(text = "Мої контакти", fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black)
                            Text(text = "Змінити налаштування контактів", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = "Notification Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(text = "Повідомлення", fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black)
                            Text(text = "Змінити налаштування повідомлень", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.qa),
                            contentDescription = "Help Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(text = "Конфіденційність", fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black)
                            Text(text = "Налаштування конфіденційністі користувача", fontSize = 14.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.other),
                            contentDescription = "Future Feature Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(text = "Майбутня фіча)", fontWeight = FontWeight.Normal, fontSize = 18.sp, color = Color.Black)
                            Text(text = "Майбутня фіча,яка змінить світ на краще", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }


            }
        }


        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }



        BottomNavigation(
            backgroundColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {

            BottomNavigationItem(
                selected = true,
                onClick = { onNavigateToMessageScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.message),
                        contentDescription = "Messages Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Повідомлення",
                        fontSize = 10.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { onNavigateToCallScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calls),
                        contentDescription = "Calls Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Дзвінки",
                        fontSize = 12.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = { onNavigateToProfileScreen() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Профіль",
                        fontSize = 12.sp
                    )
                }
            )

            BottomNavigationItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Налаштування",
                        fontSize = 11.sp
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(senderName: String, onBack: () -> Unit) {
    val text = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.Black),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.avatar8),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = senderName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "онлайн",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF8F8F8)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD54F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "plus",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    OutlinedTextField(
                        value = text.value,
                        onValueChange = {text.value = it},
                        placeholder = { Text("Ваш текст...", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        ),
                        maxLines = 1
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD54F)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.send),
                            contentDescription = "send message",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }


        }
    }
}




