package com.example.vocabwidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle

class VocabWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val word = VocabRepository.getRandomWord()
        
        provideContent {
            GlanceTheme {
                WidgetContent(word)
            }
        }
    }

    @Composable
    private fun WidgetContent(word: Word) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = word.term,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = GlanceTheme.colors.primary,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = word.definition,
                style = TextStyle(
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = GlanceTheme.colors.onSurfaceVariant
                ),
                maxLines = 3
            )
            Spacer(modifier = GlanceModifier.height(16.dp))
            Button(
                text = "Next Word",
                onClick = actionRunCallback<UpdateWordCallback>()
            )
        }
    }
}

class UpdateWordCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        VocabWidget().update(context, glanceId)
    }
}

class VocabWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = VocabWidget()
}
