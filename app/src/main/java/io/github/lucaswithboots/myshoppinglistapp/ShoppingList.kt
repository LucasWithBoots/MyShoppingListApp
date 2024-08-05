package io.github.lucaswithboots.myshoppinglistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp(innerPadding: PaddingValues) {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item!")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(item) { name, quantity ->
                        sItems = sItems.map {
                            if (it.id == item.id) {
                                it.copy(name = name, quantity = quantity, isEditing = false)
                            } else {
                                it
                            }
                        }
                    }
                } else {
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        }, onDeleteClick = {
                            sItems = sItems - item
                        })
                }

            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            sItems = sItems + (ShoppingItem(
                                id = sItems.size + 1,
                                name = itemName,
                                quantity = itemQuantity.toIntOrNull() ?: 1
                            ))

                            showDialog = false

                            itemName = ""
                            itemQuantity = ""
                        }

                    }) {
                        Text(text = "Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(text = "Cancel")
                    }
                },
                title = { Text(text = "Add Shopping Item") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            label = { Text(text = "Item name") }
                        )

                        OutlinedTextField(
                            value = itemQuantity,
                            onValueChange = { itemQuantity = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            label = { Text(text = "Quantity") }
                        )
                    }
                })
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.name)
            Text(text = "Qty: ${item.quantity}")
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }


    }
}

@Composable
fun ShoppingItemEditor(
    item: ShoppingItem,
    onEditComplete: (String, Int) -> Unit
) {
    var editedName by remember { mutableStateOf("") }
    var editedQuantity by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.width(200.dp)
        ) {
            OutlinedTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                label = { Text(text = "Name") }
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                label = { Text(text = "Quantity") }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }
        ) {
            Text(text = "Save")
        }
    }
}

@Preview
@Composable
fun ShoppingListItemPreview() {
    val item = ShoppingItem(
        id = 1,
        name = "Teste",
        quantity = 19
    )

    ShoppingListItem(item, {}, {})
}

@Preview
@Composable
fun ShoppintItemEditorPreview() {
    val item = ShoppingItem(
        id = 1,
        name = "Teste",
        quantity = 19
    )

    ShoppingItemEditor(item, { name, quantity -> (Unit) })
}