/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ai.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MenuItem(
    val routeId: String,
    val titleResId: Int,
    val descriptionResId: Int,
    val img: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onItemClicked: (String) -> Unit = { }
) {
    val PageHeader="SAGE TALK"
    val menuItems = listOf(
        MenuItem("summarize", R.string.menu_summarize_title, R.string.menu_summarize_description,R.drawable.asksage),
        MenuItem("photo_reasoning", R.string.menu_reason_title, R.string.menu_reason_description,R.drawable.whatisthis),
        MenuItem("chat", R.string.menu_chat_title, R.string.menu_chat_description,R.drawable.sagechat)
    )
    Text(text = PageHeader, fontSize = 30.sp,color= Color(0xffffffff), modifier = Modifier
        .padding(all = 16.dp)
        .fillMaxWidth())
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF00244A) // Set background color to white
    ){
    LazyColumn(
        Modifier
            .padding(top = 40.dp, bottom = 16.dp)
    ) {

        items(menuItems) { menuItem ->
            Card(
                onClick = {
                    onItemClicked(menuItem.routeId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(Color(0xBBffffff))
            ) { Row {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .width(200.dp)
                ) {
                    Text(
                        color = Color(0xFF00244A),
                        fontSize = 25.sp,
                        text = stringResource(menuItem.titleResId),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(menuItem.descriptionResId),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    }
                Image(painter = painterResource(id = menuItem.img), contentDescription = "Images",
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp))
            }

            }

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen()
}
