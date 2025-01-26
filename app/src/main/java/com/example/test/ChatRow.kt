package com.example.test

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatRow(chatMessage: ChatMessage, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .clickable { onClick() }, // Добавляем обработчик клика
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = chatMessage.avatarRes),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))


        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chatMessage.senderName,
                style = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = chatMessage.message,
                style = TextStyle(fontSize = 14.sp, color = Color.Gray)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = chatMessage.time,
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )
            Icon(
                painter = painterResource(id = R.drawable.check),
                contentDescription = "Message status",
                tint = Color.Gray,
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp)
            )
        }
    }
}
