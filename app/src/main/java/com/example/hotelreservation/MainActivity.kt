package com.example.hotelreservation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- FONT ----------
val robotoSlab = FontFamily(
    Font(R.font.roboto_slab, FontWeight.Normal)
)

// ---------- REGION ----------
enum class Region {
    SPA, MAZURY, GORY, MORZE
}

// ---------- MODEL ----------
data class Resort(
    val name: String,
    val city: String,
    val imageRes: Int,
    val url: String,
    val region: Region
)

// ---------- DATA ----------
val resorts = listOf(

    // SPA
    Resort("White Hills", "Białka Tatrzańska", R.drawable.white_hills,
        "https://www.booking.com/hotel/pl/white-hills-loftaffair-collection.pl.html", Region.SPA),

    Resort("Destigo Hotels", "Mikołajki", R.drawable.destigo_hotels,
        "https://www.booking.com/hotel/pl/apartamenty-n11-mikolajki.pl.html", Region.SPA),

    Resort("Termy Karkonosze", "Staniszów", R.drawable.termy_karkonosze,
        "https://www.booking.com/hotel/pl/termy-karkonosze-resort-amp-spa.pl.html", Region.SPA),

    Resort("Muse Spa", "Kraków", R.drawable.muse_spa,
        "https://www.booking.com/hotel/pl/the-muse-by-loft-affair.pl.html", Region.SPA),

    Resort("Aries Hotel", "Szczyrk", R.drawable.aries_hotel,
        "https://www.booking.com/hotel/pl/aries-amp-spa-szczyrk.pl.html", Region.SPA),

    Resort("Slow Tatry", "Zakopane", R.drawable.slow_tatry,
        "https://www.booking.com/hotel/pl/slow-tatry.pl.html", Region.SPA),

    // MAZURY
    Resort("Glemuria", "Kętrzyn", R.drawable.glemuria_mazury,
        "https://www.booking.com/hotel/pl/glemuria.pl.html", Region.MAZURY),

    Resort("Domek na Skarpie", "Olsztynek", R.drawable.domek_mazury,
        "https://www.booking.com/hotel/pl/domek-na-skarpie-olsztynek.pl.html", Region.MAZURY),

    Resort("Jabłoń Resort", "Pisz", R.drawable.jablon_mazury,
        "https://www.booking.com/hotel/pl/jablon.pl.html", Region.MAZURY),

    Resort("Przystań Hotel", "Olsztyn", R.drawable.przystan_mazury,
        "https://www.booking.com/hotel/pl/przystan-amp-spa.pl.html", Region.MAZURY),

    Resort("Ruciane Park", "Ruciane-Nida", R.drawable.ruciane_mazury,
        "https://rucianepark.pl/", Region.MAZURY),

    Resort("Lake Hill Mazury", "Ostróda", R.drawable.hill_mazury,
        "https://www.lakehillmazury.pl/", Region.MAZURY),

    // GÓRY
    Resort("Bania Thermal", "Białka Tatrzańska", R.drawable.bania,
        "https://www.booking.com/hotel/pl/hotel-bania-bialka-tatrzanska.pl.html", Region.GORY),

    Resort("Aparthotel Góralski", "Białka Tatrzańska", R.drawable.aparthotel,
        "https://www.booking.com/hotel/pl/aparthotel-goralski-spa.pl.html", Region.GORY),

    Resort("Hotel Górski", "Białka Tatrzańska", R.drawable.hotel_gorski,
        "https://www.booking.com/hotel/pl/osrodek-gorski.pl.html", Region.GORY),

    Resort("Willa Stożek", "Białka Tatrzańska", R.drawable.willa_stozek,
        "https://www.booking.com/hotel/pl/willa-stozek-bialka-tatrzanska.pl.html", Region.GORY),

    Resort("Grand Tatry", "Białka Tatrzańska", R.drawable.grand_tatry,
        "https://www.booking.com/hotel/pl/grand-tatry.pl.html", Region.GORY),

    Resort("Hotel Toporów", "Białka Tatrzańska", R.drawable.hotel_toporow,
        "https://www.booking.com/hotel/pl/pensjonat-toporow.pl.html", Region.GORY),

    // MORZE
    Resort("SEO2 Rewal", "Rewal", R.drawable.rewal,
        "https://www.booking.com/hotel/pl/seo2-rewal.pl.html", Region.MORZE),

    Resort("Resort Król Plaza", "Jarosławiec", R.drawable.resort_plaza,
        "https://www.booking.com/hotel/pl/resort-krol-plaza-spa-amp-wellness.pl.html", Region.MORZE),

    Resort("Marina", "Darłówko", R.drawable.marina,
        "https://www.booking.com/hotel/pl/apartamenty-royale.pl.html", Region.MORZE),

    Resort("Białe Piaski", "Jastarnia", R.drawable.biale_piaski,
        "https://www.booking.com/hotel/pl/apartamenty-biale-piaski.pl.html", Region.MORZE),

    Resort("Perłowa Przystań", "Sianożęty", R.drawable.perlowa_przystan,
        "https://www.booking.com/hotel/pl/zachod-slonca-z-widokiem-na-morze-320-perlowa-przystan-holiday-city.pl.html", Region.MORZE),

    Resort("Błękitne Wzgórze", "Władysławowo", R.drawable.blekitne_wzgorze,
        "https://www.booking.com/hotel/pl/pokoje-goscinne-blekitne-wzgorze.pl.html", Region.MORZE)
)

// ---------- MAIN ----------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var selectedRegion by remember { mutableStateOf<Region?>(null) }

            when (selectedRegion) {
                null -> HomeScreen { selectedRegion = it }
                else -> ResortsListScreen(
                    region = selectedRegion!!,
                    onBack = { selectedRegion = null }
                )
            }
        }
    }
}

// ---------- HOME ----------
@Composable
fun HomeScreen(onRegionClick: (Region) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RegionTile("SPA", R.drawable.spa) { onRegionClick(Region.SPA) }
            Spacer(Modifier.height(16.dp))
            RegionTile("Mazury", R.drawable.mazury) { onRegionClick(Region.MAZURY) }
            Spacer(Modifier.height(16.dp))
            RegionTile("Góry", R.drawable.gory) { onRegionClick(Region.GORY) }
            Spacer(Modifier.height(16.dp))
            RegionTile("Morze", R.drawable.morze) { onRegionClick(Region.MORZE) }
        }
    }
}

// ---------- REGION TILE ----------
@Composable
fun RegionTile(title: String, image: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(260.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {

        Image(
            painter = painterResource(image),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Text(
            text = title,
            color = Color.White,
            fontFamily = robotoSlab,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

// ---------- LISTA RESORTÓW ----------
@Composable
fun ResortsListScreen(region: Region, onBack: () -> Unit) {
    val context = LocalContext.current
    val filtered = resorts.filter { it.region == region }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F3ED))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Cofnij",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onBack() }
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filtered) { resort ->
                Column(
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(resort.url))
                        )
                    }
                ) {

                    Box(
                        modifier = Modifier
                            .height(160.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {

                        Image(
                            painter = painterResource(resort.imageRes),
                            contentDescription = resort.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.25f))
                        )

                        Text(
                            text = resort.name,
                            color = Color.White,
                            fontFamily = robotoSlab,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = resort.city,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}












