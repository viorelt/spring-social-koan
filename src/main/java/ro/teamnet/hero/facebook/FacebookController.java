/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.teamnet.hero.facebook;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SLF4JLogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/facebook")
public class FacebookController {
    public static final Log LOG = SLF4JLogFactory.getLog(FacebookController.class);
    private final Facebook facebook;
    public static HttpHeaders headers = new HttpHeaders();

    static {
        headers.add("Content-Type", "application/json; charset=utf-8");
    }

    @Inject
    public FacebookController(Facebook facebook) {
        this.facebook = facebook;
    }

    @RequestMapping(value = "/feed", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showFeed() {
        PagedList<Post> feeds = facebook.feedOperations().getFeed();
        String feedsJson = new JSONSerializer().exclude("*.class").serialize(feeds);

        return new ResponseEntity<String>(feedsJson, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/feed", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> postUpdate(@RequestBody String json) {
        Map<String, String> mess = new JSONDeserializer<Map<String, String>>().deserialize(json);
        String message = mess.get("message");
        facebook.feedOperations().updateStatus(message);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showFriends() {
        PagedList<FacebookProfile> facebookProfiles =
                facebook.friendOperations().getFriendProfiles();
        String ret = new JSONSerializer().exclude(".class").serialize(facebookProfiles);
        return new ResponseEntity<String>(ret, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/albums", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showAlbums() {
        PagedList<Album> albums = facebook.mediaOperations().getAlbums();
        String ret = new JSONSerializer().exclude(".class").serialize(albums);
        return new ResponseEntity<String>(ret, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/album/{albumId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showAlbum(@PathVariable("albumId") String albumId) {
        Album album = facebook.mediaOperations().getAlbum(albumId);
        PagedList<Photo> photos = facebook.mediaOperations().getPhotos(albumId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("album", album);
        map.put("photos", photos);
        String ret = new JSONSerializer().exclude(".class").serialize(map);
        return new ResponseEntity<String>(ret, headers, HttpStatus.OK);
    }

    @Inject
    private ConnectionRepository connectionRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> profile(HttpServletRequest request, HttpServletResponse response) {
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
        String ret = new JSONSerializer().exclude(".class").serialize(connection);
        if (connection == null) {
            try {
                response.sendRedirect("/connect/facebook");
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return new ResponseEntity<String>(ret, headers, HttpStatus.OK);
    }

}
