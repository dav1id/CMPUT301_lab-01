package com.example.listycity

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

import com.example.listycity.ui.theme.ListyCityTheme


// Activities are like the URL Pages
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cityRepository = CityRepository()

        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onRemoveCity = {cityRepository.removeCity(it)},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable // a function that allows us to show ui
fun CityListScreen(cities: List<String>,
                   onAddCity: (String) -> Unit,
                   onRemoveCity: (String) -> Unit,
                   modifier: Modifier = Modifier){

    var newCityName by remember {mutableStateOf("")}
    var cityStateAdd: Boolean = false



    Column(modifier = modifier.fillMaxSize()){
        Row(modifier = Modifier.padding(16.dp)){
            OutlinedTextField(
                value = newCityName,
                onValueChange = {newCityName = it},
                label = { Text("Type Here")},
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { cityStateAdd = true }
            ){
                Text("Add City")
            }

            Button(
                onClick = {
                    if (newCityName.isNotBlank()){
                        onRemoveCity(newCityName)
                        newCityName = ""
                    }
                }
            ){
                Text("Remove City")
            }
        }

        Row(modifier = Modifier.padding(16.dp)){
            Button(
                onClick = {
                    if (newCityName.isNotBlank() && cityStateAdd){
                        onAddCity(newCityName) // Why not just call the function cityRepositoryObject.addCity() here??
                        cityStateAdd = false
                    }
                }
            ){
                Text("Confirm")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(cities) { // re-executes the code again when a new item is added/removed
                city -> CityRow(city = city, onRemoveCity)
            }
        }
    }
}

@Composable
fun CityRow(city: String, onRemoveCity: (String) -> Unit){
    Button(
        onClick = {
           onRemoveCity(city)
        }
    ){
        Text(
            text = city,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal=18.dp, vertical=14.dp)
        )
    }
}

class CityRepository {
    private val _cities = mutableStateListOf("edmonton", "vancouver", "moscow", "sydney", "berlin", "" +
            "vienna", "tokyo", "osaka", "new delhi") // allows for the lazy column to be updated with the new city

    val cities: List<String>
        get() = _cities

    fun addCity(cityName: String): Unit {
        _cities.add(cityName)
    }

    fun removeCity(cityName: String): Unit {
        _cities.remove(cityName)
    }

}



