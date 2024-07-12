package com.example.new_practice.app

import com.example.new_practice.app.Quality

interface ClickQualityListener {
    operator fun invoke(quality: Quality?)
}