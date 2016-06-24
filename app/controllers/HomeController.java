package controllers;

import io.onetapbeyond.opencpu.r.executor.*;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import play.mvc.*;
import views.html.*;
import play.Logger;
import play.Logger.ALogger;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    final Logger.ALogger log = Logger.of(this.getClass());

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render());
    }

    public Result plot() {

        Map data = new HashMap();
        data.put("plotWidth", 750);
        data.put("plotHeight", 320);

        OCPUTask oTask = null;

        try {

            oTask = OCPU.R()
                        .pkg("mcs.poc")
                        .function("render")
                        .input(data)
                        .library();
        } catch(Exception oex) {
            log.warn("plot: ex={}", oex);
        }

        String plotAsString = "";

        if(oTask != null) {

            OCPUResult oResult = oTask.execute(OCPU_ENDPOINT);
            log.info("plot: result isSuccess={}", oResult.success());
            log.info("plot: result isError={}", oResult.error());
            log.info("plot: result cause={}", oResult.cause());
            if(oResult.output() != null) {

                ArrayList plot =
                    (ArrayList) oResult.output().get("render");
                plotAsString = (String) plot.get(0);
                log.info("plot: result plotAsString={}", plotAsString);
            }

        }

        return ok(plotAsString.getBytes()).as("image/png; charset=utf-8; base64,");
    }

    private final String OCPU_ENDPOINT = "http://localhost:4191/ocpu";

}
