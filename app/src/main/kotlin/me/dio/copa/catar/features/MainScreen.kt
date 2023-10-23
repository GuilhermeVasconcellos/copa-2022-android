package me.dio.copa.catar.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.dio.copa.catar.R
import me.dio.copa.catar.domain.extensions.getDate
import me.dio.copa.catar.domain.model.MatchDomain
import me.dio.copa.catar.domain.model.TeamDomain
import me.dio.copa.catar.ui.theme.Shapes

typealias NotificationOnClick = (match: MatchDomain) -> Unit

@Composable
fun MainScreen(matches: List<MatchDomain>, onNotificationClick: NotificationOnClick) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
//        .background(color = Color.LightGray)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                AsyncImage(
                    model = R.drawable.capa_3,
                    contentDescription = "imagem da logo da copa",
                    modifier = Modifier.height(160.dp),
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(matches) {
                        MatchInfo(it, onNotificationClick)
                    }
                }
            }

        }
    }
}

@Composable
fun Notification(match: MatchDomain, onClick: NotificationOnClick) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        val drawable = if(match.notificationEnabled) R.drawable.ic_notifications_active else R.drawable.ic_notifications
        Image(
            painter = painterResource(id = drawable),
            contentDescription = "imagem de notificação",
            modifier = Modifier.clickable {
                onClick(match)
            }
        )
    }
}

@Composable
fun Title(match: MatchDomain) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(text = "${match.date.getDate()} - ${match.name}", style = MaterialTheme.typography.h6.copy(color = Color.White))
    }
}

@Composable
fun Teams(match: MatchDomain) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        TeamItem(team = match.team1)
        Text(text = "X", modifier = Modifier.padding(start = 24.dp, end = 24.dp), style = MaterialTheme.typography.h6.copy(color = Color.White))
        TeamItem(team = match.team2)
    }
}

@Composable
fun TeamItem(team: TeamDomain) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = team.flag, modifier = Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.h3.copy(color = Color.White))
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = team.displayName, modifier = Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.h6.copy(color = Color.White))
    }
}

@Composable
fun MatchInfo(match: MatchDomain, onNotificationClick: NotificationOnClick) {
    Card(
        shape = Shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box {
            AsyncImage(
                model = match.stadium.image,
                contentDescription = "imagem do estádio",
                modifier = Modifier.height(160.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Notification(match, onNotificationClick)
                Title(match)
                Teams(match)
            }
            
        }
    }
}
