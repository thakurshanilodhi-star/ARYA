package com.aria.app
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.*
import android.speech.tts.TextToSpeech
import java.util.*
class VoiceEngine(private val context:Context): TextToSpeech.OnInitListener {
    private val recognizer: SpeechRecognizer? = try { if (SpeechRecognizer.isRecognitionAvailable(context)) SpeechRecognizer.createSpeechRecognizer(context) else null } catch (e: Exception) { null }
    private val tts=TextToSpeech(context,this)
    var ttsReady=false
    var onResult:((String)->Unit)?=null
    init { recognizer?.setRecognitionListener(object:RecognitionListener{
        override fun onResults(r:Bundle){r.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.let{onResult?.invoke(it)}}
        override fun onError(e:Int){}
        override fun onReadyForSpeech(p:Bundle){};override fun onBeginningOfSpeech(){};override fun onRmsChanged(v:Float){};override fun onBufferReceived(b:ByteArray){}
        override fun onEndOfSpeech(){};override fun onPartialResults(b:Bundle){};override fun onEvent(t:Int,b:Bundle){}
    })}
    override fun onInit(status:Int){ttsReady=status==TextToSpeech.SUCCESS;if(ttsReady)tts.language=Locale.US}
    fun listen():Boolean{if(context.checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)return false;recognizer?.startListening(IntentFactory.voiceIntent());return true}
    fun speak(text:String){if(ttsReady)tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"aria-reply")}
    fun release(){recognizer?.destroy();tts.shutdown()}
}
object IntentFactory{fun voiceIntent()=android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())}}
