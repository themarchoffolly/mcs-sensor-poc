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
 * Main controller for MCS Sensor POC application.
 */
public class HomeController extends Controller {

    final Logger.ALogger log = Logger.of(this.getClass());

    /*
     * Returns MCS Sensor POC home page.
     */
    public Result index() {
        return ok(index.render());
    }

    /*
     * Returns R MCS.Sensor::render base64 encoded plot data.
     */
    public Result plot() {

        /*
         * Default MCS.sensor::render plot dimensions. These
         * dimensions can be customized as required by POC.
         */
        Map data = new HashMap();
        data.put("plotWidth", 750);
        data.put("plotHeight", 320);

        OCPUTask oTask = null;
        String plotData = "";

        try {

            /*
             * Define OCPUTask for MCS.Sensor::render function call.
             */
            oTask = OCPU.R()
                        .pkg("mcs.poc")
                        .function("render")
                        .input(data)
                        .library();

            /*
             * Execute OCPUTask for MCS.Sensor::render function call.
             */
            OCPUResult oResult = oTask.execute(OCPU_ENDPOINT);

            /*
             * Handle OCPUResult for MCS.Sensor::render function call.
             */
            if(oResult.output() != null) {

                /*
                 * Extract base64 encoded plot data from OCPUResult.
                 */
                ArrayList plot =
                    (ArrayList) oResult.output().get("render");
                plotData = (String) plot.get(0);
            }

        }

        } catch(Exception oex) {
            log.warn("plot: ex={}", oex);
        }

        /*
         * Returns R MCS.Sensor::render base64 encoded plot data.
         */
        return ok(plotData.getBytes()).as(BASE64_ENCODING);
    }

    private final String OCPU_ENDPOINT = "http://localhost:4191/ocpu";
    private final String BASE64_ENCODING = "image/png; charset=utf-8; base64,";

}
