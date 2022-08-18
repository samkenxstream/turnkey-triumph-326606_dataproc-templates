/*
 * Copyright (C) 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.dataproc.templates.redshift;

import static com.google.cloud.dataproc.templates.util.TemplateConstants.*;

import com.google.cloud.dataproc.templates.BaseTemplate;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedshiftToGCS implements BaseTemplate {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedshiftToGCS.class);

  @Override
  public void runTemplate() {
    SparkSession spark = SparkSession.builder().appName("Spark HiveToGcs Job").getOrCreate();
    LOGGER.info("RedshiftToGcs job started.");

    spark.sparkContext().hadoopConfiguration().set("fs.s3n.awsAccessKeyId", "AKIAS6LKVVNKHFJBAVV2");
    spark
        .sparkContext()
        .hadoopConfiguration()
        .set("fs.s3n.awsSecretAccessKey", "VTHvQdo/4zcZh1EEq1b6uctj4YjJ+SC2B8VW9EWs");

    Dataset<Row> df =
        spark
            .read()
            .format("com.databricks.spark.redshift")
            .option("url", "jdbc:redshift://redshifthost:5439/database?user=username&password=pass")
            .option("dbtable", "table_name")
            .option("tempdir", "s3n://path/for/temp/data")
            .load();

    df.show();

    LOGGER.info("RedshiftToGcs job completed.");
    spark.stop();
  }
}
