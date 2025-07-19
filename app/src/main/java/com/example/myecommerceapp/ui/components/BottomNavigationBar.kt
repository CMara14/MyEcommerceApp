package com.example.myecommerceapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.MyEcommerceAppTheme
import com.example.myecommerceapp.ui.theme.PinkPastel

import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = DarkBackground,
        contentColor = LightGrayText
    ) {
        val items = listOf("Home", "Orders", "Cart", "Profile")
        val icons = listOf(Icons.Filled.Home, Icons.Filled.LocalMall, Icons.Filled.ShoppingCart, Icons.Filled.Person)

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPastel,
                    selectedTextColor = PinkPastel,
                    unselectedIconColor = LightGrayText,
                    unselectedTextColor = LightGrayText,
                    indicatorColor = DarkBackground
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    MyEcommerceAppTheme {
        var selected by remember { mutableStateOf(1) }
        BottomNavigationBar(
            selectedIndex = selected,
            onItemSelected = { index -> selected = index }
        )
    }
}
