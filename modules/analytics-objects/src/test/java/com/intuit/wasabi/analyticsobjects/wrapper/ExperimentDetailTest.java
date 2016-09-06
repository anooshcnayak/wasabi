/*******************************************************************************
 * Copyright 2016 Intuit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.intuit.wasabi.analyticsobjects.wrapper;

import com.intuit.wasabi.experimentobjects.Application;
import com.intuit.wasabi.experimentobjects.Bucket;
import com.intuit.wasabi.experimentobjects.Experiment;

import static cern.jet.math.Functions.exp;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * This is the test class for {@link ExperimentDetail}.
 */
public class ExperimentDetailTest {

    Experiment.ID expId = Experiment.ID.newInstance();
    Experiment.Label expLabel = Experiment.Label.valueOf("TestLabelForExperimentDetail");
    Experiment.State expState = Experiment.State.RUNNING;
    Application.Name appName = Application.Name.valueOf("TestApplicationLabelForExperimentDetail");
    Calendar modTime = Calendar.getInstance();
    Calendar startTime = Calendar.getInstance();

    {
        modTime.set(2016,8,16);
        startTime.set(2016,8,1);
    }

    Experiment exp = Experiment.withID(expId).withApplicationName(appName).withModificationTime(modTime.getTime())
            .withLabel(expLabel).withStartTime(startTime.getTime()).withState(Experiment.State.TERMINATED).build();


    @Test
    public void testConstructor(){
        ExperimentDetail expDetail = new ExperimentDetail(expId, expState, expLabel, appName, modTime.getTime(),
                startTime.getTime());
        assertEquals(expDetail.getAppName(), appName);
        assertEquals(expDetail.getId(), expId);
        assertEquals(expDetail.getLabel(), expLabel);
        assertEquals(expDetail.getState(), expState);
        assertEquals(expDetail.getModificationTime(), modTime.getTime());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstraintsId(){
        new ExperimentDetail(null, expState, expLabel, appName, modTime.getTime(), startTime.getTime());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstraintsState(){
        new ExperimentDetail(expId, null, expLabel, appName, modTime.getTime(), startTime.getTime());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstraintsLabel(){
        new ExperimentDetail(expId, expState, null, appName, modTime.getTime(), startTime.getTime());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstraintsAppName(){
        new ExperimentDetail(expId, expState, expLabel, null, modTime.getTime(), startTime.getTime());
    }

    @Test
    public void testCreationFromExperiment(){

        ExperimentDetail expDetail = new ExperimentDetail(exp);

        assertEquals(exp.getID(), expDetail.getId());
        assertEquals(exp.getApplicationName(), expDetail.getAppName());
        assertEquals(exp.getModificationTime(), expDetail.getModificationTime());
        assertEquals(exp.getLabel(), expDetail.getLabel());
        assertEquals(exp.getStartTime(), expDetail.getStartTime());
        assertEquals(exp.getState(), expDetail.getState());

    }

    @Test
    public void testBuckets(){
        List<Bucket> buckets = new ArrayList<>();
        Bucket bucketA = Bucket.newInstance(expId, Bucket.Label.valueOf("BucketA"))
                .withAllocationPercent(0.8).withControl(true).build();
        Bucket bucketB = Bucket.newInstance(expId, Bucket.Label.valueOf("BucketB"))
                .withAllocationPercent(0.1).withControl(false).build();
        Bucket bucketC = Bucket.newInstance(expId, Bucket.Label.valueOf("BucketC"))
                .withAllocationPercent(0.1).withControl(false).build();
        buckets.add(bucketA);
        buckets.add(bucketB);
        buckets.add(bucketC);

        ExperimentDetail expDetail = new ExperimentDetail(exp);

        expDetail.addBuckets(buckets);

        assertEquals(expDetail.getBuckets().size(), 3);

    }
}
