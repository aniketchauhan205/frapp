package com.example.landingpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.TextField

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp


@Composable
fun LoginScreen() {
    val backgroundImage: Painter = painterResource(id = R.drawable.bg)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = null, // Provide a content description if needed
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp), // Adjust padding as needed
            contentScale = ContentScale.Crop // Crop the image to fill the space
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "please fill your information below", fontSize = 10.sp)

            Spacer(modifier = Modifier.height(20.dp))

            TextField(value = "", onValueChange = {}, label = { Text(text = "E-mail") })

            Spacer(modifier = Modifier.height(20.dp))

            TextField(value = "", onValueChange = {}, label = { Text(text = "Password") })

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 65.dp)
            ) {
                Text(text = "Login")

            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 65.dp, end = 65.dp)
            ) {
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterEnd)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Forgot password?",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 65.dp)
                    .clickable { },
                fontSize = 13.sp
            )


        }

    }
}
