package com.yd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yd.persistence.repository.TwitterDataRepository;
import com.yd.persistence.repository.model.TwitterData;

@Controller
@Profile("twitter-api")
public class TwitterController {

	@Autowired
	private Twitter twitter;
	@Autowired
	private TwitterDataRepository twitterDataRepository;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ModelAndView home() {
		List<TwitterData> twitterDatas = twitterDataRepository
				.findAllByOrderByPostingDateAsc();
		ModelAndView model = new ModelAndView();
		model.addObject("twitter_datas", twitterDatas);
		model.setViewName("home");
		return model;
	}

}
