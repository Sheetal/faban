/* The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://www.sun.com/cddl/cddl.html or
 * install_dir/legal/LICENSE
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at install_dir/legal/LICENSE.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * $Id: Cpustat.java,v 1.13 2009/06/26 18:30:13 akara Exp $
 *
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */
package com.sun.faban.harness.tools;

import com.sun.faban.common.Command;

import java.io.IOException;

/**
 * Cpustat starts the cpustat tool. Unlike GenericTool, cpustat needs some
 * further postprocessing before the data is useful.
 *
 * @author Akara Sucharitakul
 * @see com.sun.faban.harness.tools.Tool
 */
public class Cpustat extends CommandLineTool{

    private long stopTime;

    @Configure
    public void configure() {
        super.config();
        // The postprocessed output is in .xan.host file
        String rawFile = ctx.getOutputFile();
        ctx.setOutputFile(rawFile.replace(".log.", ".raw."));
    }


    /**
     * This method is responsible for stopping the tool utility.
     */
    @Stop
    public void stop() throws IOException, InterruptedException {
        super.stop();
        stopTime = System.currentTimeMillis();
    }

    @Postprocess
    public void postprocess() throws IOException, InterruptedException {
        String rawFile = ctx.getOutputFile();
        String postFile = rawFile.replace(".raw.", ".xan.");
        ctx.setOutputFile(postFile);
        long sleepTime = stopTime + 500 - System.currentTimeMillis();
        if (sleepTime > 0)
            Thread.sleep(sleepTime);
        cmd = new Command("cpustat-post");
        ctx.exec(cmd, true);
    }
}
