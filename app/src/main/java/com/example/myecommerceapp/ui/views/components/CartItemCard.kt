package com.example.myecommerceapp.ui.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.MyEcommerceAppTheme
import com.example.myecommerceapp.ui.theme.PinkPastel
import com.example.myecommerceapp.ui.theme.White
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartItemCard(
    item: CartItem,
    onQuantityChange: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "AR"))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = InputFieldColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium.copy(color = White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))


                Text(
                    text = "Price: ${currencyFormatter.format(item.unitPrice)}",
                    style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText)
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { if (item.quantity > 1) onQuantityChange(item.productId, item.quantity - 1) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Filled.Remove, contentDescription = "Decrease quantity", tint = PinkPastel)
                    }
                    Text(
                        text = "${item.quantity}",
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { onQuantityChange(item.productId, item.quantity + 1) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Increase quantity", tint = PinkPastel)
                    }
                }
            }


            IconButton(
                onClick = { onRemoveItem(item.productId) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Remove item", tint = LightGrayText)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemCardPreview() {
    MyEcommerceAppTheme {
        CartItemCard(
            item = CartItem(
                productId = "prod123",
                name = "Example Product Name",
                unitPrice = 123456.78,
                imageUrl = "https://via.placeholder.com/150",
                quantity = 3
            ),
            onQuantityChange = { _, _ -> },
            onRemoveItem = {}
        )
    }
}