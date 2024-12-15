package mx.cdmadero.tecnm.Walmart.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LandingController {

    @GetMapping("/")
    fun showLandingPage(model: Model): String {
        return "landing"
    }
}
