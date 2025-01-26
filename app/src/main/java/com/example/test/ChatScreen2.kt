package com.example.test

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ChatScreen2(onChatClick: (ChatMessage) -> Unit) {
    val chatMessages = listOf(
        ChatMessage("El kamcy", "okay sure!!", R.drawable.avatar8, "12:25"),
        ChatMessage("Aisha", "How are you?", R.drawable.avatar5, "12:30"),
        ChatMessage("Joyce", "Let's meet up!", R.drawable.avatar6, "12:45")
    )

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Всі чати",
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        )


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            items(chatMessages) { message ->
                ChatRow(chatMessage = message, onClick = { onChatClick(message) })
            }
        }

    }

}
