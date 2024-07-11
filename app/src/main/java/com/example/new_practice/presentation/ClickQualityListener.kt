package com.example.new_practice.presentation

import com.example.new_practice.presentation.Quality

interface ClickQualityListener {
    operator fun invoke(quality: Quality?)
}