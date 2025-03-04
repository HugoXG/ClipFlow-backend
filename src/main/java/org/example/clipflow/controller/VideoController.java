package org.example.clipflow.controller;

import org.example.clipflow.entity.video.Video;
import org.example.clipflow.utils.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/luckyjourney/video")
public class VideoController {

    @PostMapping
    public R publishVideo(@RequestBody @Validated Video video) {
        return R.success();
    }
}
