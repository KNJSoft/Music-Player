package com.knjsoft.musicplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import java.io.IOException

class Lecteur : AppCompatActivity() {
    private lateinit var duree_ecoule: TextView
    private lateinit var durre:TextView
    private lateinit var temps: SeekBar
    private lateinit var titre_lec: TextView
    private lateinit var auteur_lec: TextView
    private lateinit var play: Button
    private lateinit var volume: SeekBar
    var music: MediaPlayer? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecteur)
        val son=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getSerializableExtra("son",Son::class.java)
        }else{
            intent.getSerializableExtra("son") as Son?
        }
        duree_ecoule=findViewById(R.id.dureecoule)
        durre=findViewById(R.id.duree)
        temps=findViewById(R.id.temps)
        titre_lec=findViewById(R.id.titrelecteur)
        auteur_lec=findViewById(R.id.auteurlecteur)
        play=findViewById(R.id.play)
        volume=findViewById(R.id.volume)
        titre_lec.text=son!!.titre
        auteur_lec.text=son.auteur
        music=MediaPlayer()
        try {
            music!!.setDataSource(son.path)
            music!!.prepare()
        }catch (e: IOException){
            e.printStackTrace()
        }
        music!!.isLooping=true
        music!!.seekTo(0)
        music!!.setVolume(0.5f,0.5f)
        val durree=tempstostring(music!!.duration)
        durre.text=durree
        volume.progress=50
        volume.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromuser: Boolean) {
                val vol=progress / 100f
                music!!.setVolume(vol,vol)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        temps.max=music!!.duration
        temps.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromuser: Boolean) {
                if (fromuser){
                    music!!.seekTo(progress)
                    temps.progress=progress
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        play.setOnClickListener{
            if (music!!.isPlaying){
                music!!.pause()
                play.setBackgroundResource(R.drawable.play)
            }else{
                music!!.start()
                play.setBackgroundResource(R.drawable.pause)
            }
        }

        Thread{
            while (music !=null){
                if (music!!.isPlaying){
                    try {
                        val t=music!!.currentPosition.toDouble()
                        val t_ecoule=tempstostring(t.toInt())
                        runOnUiThread{
                            duree_ecoule.text=t_ecoule
                            temps.progress=t.toInt()
                        }
                        Thread.sleep(1000)
                    }catch (e: InterruptedException){
                        break
                    }
                }
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        music!!.stop()
    }
    private fun tempstostring(time: Int): String{
        var endtime: String? =""
        val min=time /1000 / 60
        val sec=time /1000%60
        endtime="$min:"
        if (sec< 10){
            endtime+="0"
        }
        endtime+=sec
        return endtime
    }
}