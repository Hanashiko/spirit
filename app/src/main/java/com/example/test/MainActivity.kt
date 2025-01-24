package com.example.test
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
                }
            )
        }
        composable("ThirdScreen") {
            ThirdScreen(onBack = { navController.popBackStack() })
        }
        composable("ChatScreen") {
            ChatScreen(
                onBack = { navController.popBackStack() },
                onNavigateToCallScreen = { navController.navigate("CallScreen") },
                onNavigateToProfileScreen = { navController.navigate("ProfileScreen") },
                onNavigateToSettingsScreen = { navController.navigate("SettingsScreen") }
            )
        }

        composable("CallScreen") {
            CallScreen(onBack = { navController.popBackStack() })
        }

        composable("ProfileScreen") {
            ProfileScreen(onBack = { navController.popBackStack() })
        }

        composable("SettingsScreen") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}


@Composable
fun MainScreen(onNavigateToSecondScreen: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Text1(
            text = "Spirit",
            style = TextStyle(
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(top = 50.dp)
        )


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.trio),
                contentDescription = "Icons",
                modifier = Modifier.size(200.dp, 300.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text1(
                text = "Залишайтеся в безпеці\nбудь-де та будь-коли",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.security),
                    contentDescription = "Secure icon",
                    modifier = Modifier.size(20.dp)
                )
                Text1(
                    text = "Secure, private messaging",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }


        }


        Button(
            onClick = onNavigateToSecondScreen,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text1(
                text = "Перейти до чатів",
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterScreen(onBack: () -> Unit, onNavigateToThirdScreen: () -> Unit, onNavigateToChatScreen: ()-> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
                label = { Text1("Ваш пароль") },
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

            Button(
                onClick = { onNavigateToChatScreen() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.run { buttonColors(containerColor = Color(0xFFFFCB45)) }
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
                onClick = { offset ->
                    onNavigateToThirdScreen()
                },
                modifier = Modifier.fillMaxWidth() .padding(start = 68.dp)
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
            onNavigateToSettingsScreen = {}

        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(onBack: () -> Unit) {
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
                label = { Text1("Ваш пароль") },
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
                value = "",
                onValueChange = {},
                label = { Text1("Підтвердіть Ваш пароль") },
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
         }
    }
}



@Composable
fun ChatScreen(onBack: () -> Unit, onNavigateToCallScreen: () -> Unit, onNavigateToProfileScreen:() -> Unit, onNavigateToSettingsScreen:() -> Unit ) {
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

                Text1(
                    text = "Історії чату",
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(color = Color.White, fontSize = 24.sp)
                )
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .background(Color.White)
            ) {

                Column {
                    Text1(
                        text = "Список чатів",
                        style = TextStyle(color = Color.Black, fontSize = 20.sp),
                        modifier = Modifier.padding(16.dp)
                    )

                }
            }


            BottomNavigation(
                backgroundColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {

                BottomNavigationItem(
                    selected = true,
                    onClick = { /* TODO: Обработать клики */ },
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
fun CallScreen(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Дзвінки",
                style = TextStyle(color = Color.Black, fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text(text = "Назад", fontSize = 16.sp)
            }
        }
    }
}


@Composable
fun ProfileScreen(onBack: () -> Unit) {
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
                    .fillMaxSize()
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
    }
}



@Composable
fun SettingsScreen(onBack: () -> Unit) {
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
                        .padding(top = 140.dp, start = 50.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.key),
                            contentDescription = "Account Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Акаунт", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text(text = "Privacy, security, change number", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.message2),
                            contentDescription = "Chat Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Чати", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text(text = "Chat history, theme, wallpapers", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.notification),
                            contentDescription = "Notification Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Повідомлення", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text(text = "Messages, group and others", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.qa),
                            contentDescription = "Help Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Допомога", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text(text = "Help center, contact us, privacy policy", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.other),
                            contentDescription = "Future Feature Icon",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Майбутня фіча)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Text(text = "Network usage, storage usage", fontSize = 12.sp, color = Color.Gray)
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
                onClick = { /* TODO: Обработать клики */ },
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
                onClick = { /* TODO: Обработать клики */ },
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
                onClick = { /* TODO: Обработать клики */ },
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
                onClick = { /* TODO: Обработать клики */ },
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

