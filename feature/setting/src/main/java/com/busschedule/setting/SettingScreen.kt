package com.busschedule.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.busschedule.setting.component.ProfileCard
import core.designsystem.component.HeightSpacer
import core.designsystem.component.WidthSpacer
import core.designsystem.component.appbar.BackArrowAppBar
import core.designsystem.svg.IconPack
import core.designsystem.svg.myiconpack.IcTalk
import core.designsystem.theme.Primary
import core.designsystem.theme.TextMColor
import core.designsystem.theme.TextWColor
import core.designsystem.theme.mTitle
import core.designsystem.theme.rFooter

@Composable
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xFFFFFFFF)
fun SettingScreen() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        BackArrowAppBar(title = "설정") {

        }
        HeightSpacer(height = 16.dp)
        ProfileCard(image = "", text = "닉네임") {

        }
        HeightSpacer(height = 32.dp)
        WhiteRoundedCard(padding = PaddingValues(horizontal = 16.dp)) {
            Row(modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = IconPack.IcTalk,
                    contentDescription = "ic_forward",
                    modifier = Modifier.size(24.dp),
                    tint = Primary
                )
                WidthSpacer(width = 16.dp)
                Column {
                    Text(text = "개발자에게 문의하기", style = mTitle.copy(Primary))
                    HeightSpacer(height = 4.dp)
                    Text(
                        text = "오류 및 건의사항이 있을 시, 개발자 문의사항에 남겨주세요.\n" +
                                "더 나은 서비스를 위한 도움이 됩니다. ", style = rFooter.copy(TextMColor)
                    )
                }
            }

            Icon(
                imageVector = Icons.Rounded.ArrowForwardIos,
                contentDescription = "ic_forward",
                modifier = Modifier.size(24.dp),
                tint = Primary
            )
        }
    }
}


@Composable
fun WhiteRoundedCard(padding: PaddingValues, content: @Composable RowScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(padding),
        colors = CardDefaults.cardColors(containerColor = TextWColor, contentColor = TextMColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}