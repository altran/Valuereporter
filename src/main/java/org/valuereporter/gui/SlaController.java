package org.valuereporter.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="bard.lind@gmail.com">Bard Lind</a>
 */
@Controller
public class SlaController {
    private static final Logger log = LoggerFactory.getLogger(SlaController.class);

    public static final String PREFIX = "prefix";
    public static final String METHOD_NAME = "methodName";
    public static final String FROM = "from";
    public static final String TO = "to";

    @RequestMapping("/sla")
    public ModelAndView showSlaGraph(@RequestParam(value = PREFIX, required = true) String prefix, @RequestParam(value = METHOD_NAME, required = true) String methodName) {
        Map model = new HashMap<String,String>();
        model.put(PREFIX, prefix);
        model.put(METHOD_NAME, methodName);
        log.trace("Input prefix {}, methodName {}", prefix,methodName);
        return new ModelAndView("sla", "model", model);
    }
    @RequestMapping("/slahc")
    public ModelAndView showSlaGraphHighChart(@RequestParam(value = PREFIX, required = true) String prefix, @RequestParam(value = METHOD_NAME, required = true) String methodName) {
        Map model = new HashMap<String,String>();
        model.put(PREFIX, prefix);
        model.put(METHOD_NAME, methodName);
        log.trace("Input prefix {}, methodName {}", prefix,methodName);
        return new ModelAndView("slahc", "model", model);
    }

    @RequestMapping("/slainterval")
    public ModelAndView showSlaGraphInterval(@RequestParam(value = PREFIX, required = true) String prefix, @RequestParam(value = METHOD_NAME, required = true) String methodName,
                                             @RequestParam(value = FROM, required = false) Long from, @RequestParam(value = TO, required = false) String to) {
        Map model = new HashMap<String,String>();
        model.put(PREFIX, prefix);
        model.put(METHOD_NAME, methodName);
        if (from != null) {
            model.put(FROM, from);
        }
        if (to != null) {
            model.put(TO, to);
        }
        log.trace("Input prefix {}, methodName {}", prefix,methodName);
        return new ModelAndView("slainterval", "model", model);
    }
}
