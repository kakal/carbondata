/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.carbondata.query.aggregator.util;

import java.util.List;

import org.carbondata.core.carbon.metadata.datatype.DataType;
import org.carbondata.core.constants.CarbonCommonConstants;
import org.carbondata.query.aggregator.CustomMeasureAggregator;
import org.carbondata.query.aggregator.MeasureAggregator;
import org.carbondata.query.aggregator.impl.*;
import org.carbondata.query.carbon.model.CustomAggregateExpression;

/**
 * Factory class to get the measure aggregator
 */
public class MeasureAggregatorFactory {

  /**
   * Below method will be used to get the measure aggregator based on type and
   * and data type
   *
   * @param aggTypes                      Aggregation for the column
   * @param dataTypes                     data type for the column
   * @param customAggregateExpressionList custom aggregation list to get the
   *                                      custom aggregation aggregator
   * @return measure agregator for all the column
   */
  public static MeasureAggregator[] getMeassureAggregator(String[] aggTypes, DataType[] dataTypes,
      List<CustomAggregateExpression> customAggregateExpressionList) {
    MeasureAggregator[] measureAggregator = new MeasureAggregator[aggTypes.length];
    int customAggregationCounter = 0;
    for (int i = 0; i < measureAggregator.length; i++) {

      if (aggTypes[i].equalsIgnoreCase(CarbonCommonConstants.CUSTOM)) {
        measureAggregator[i] =
            (CustomMeasureAggregator) customAggregateExpressionList.get(customAggregationCounter++)
                .getAggregator().getCopy();
      } else {
        measureAggregator[i] = getAggregator(aggTypes[i], dataTypes[i]);
      }
    }
    return measureAggregator;
  }

  /**
   * Below method will be used to get the aggregate based on aggregator type
   * and aggregator data type
   *
   * @param aggregatorType aggregattor type
   * @param dataType       data type
   * @return aggregator
   */
  public static MeasureAggregator getAggregator(String aggregatorType, DataType dataType) {

    // get the MeasureAggregator based on aggregate type
    if (CarbonCommonConstants.MIN.equalsIgnoreCase(aggregatorType)) {
      switch (dataType) {
        case INT:
        case LONG:
          return new MinLongAggregator();
        case DECIMAL:
          return new MinBigDecimalAggregator();
        default:
          return new MinAggregator();
      }
    } else if (CarbonCommonConstants.COUNT.equalsIgnoreCase(aggregatorType)) {
      return new CountAggregator();
    }
    else if (CarbonCommonConstants.COUNT_STAR.equalsIgnoreCase(aggregatorType)) {
      return new CountStarAggregator();
    }
    //
    else if (CarbonCommonConstants.MAX.equalsIgnoreCase(aggregatorType)) {
      switch (dataType) {
        case INT:
        case LONG:
          return new MaxLongAggregator();
        case DECIMAL:
          return new MaxBigDecimalAggregator();
        default:
          return new MaxAggregator();
      }
    }
    //
    else if (CarbonCommonConstants.AVERAGE.equalsIgnoreCase(aggregatorType)) {
      switch (dataType) {
        case INT:
        case LONG:

          return new AvgLongAggregator();
        case DECIMAL:

          return new AvgBigDecimalAggregator();
        case TIMESTAMP:

          return new AvgTimestampAggregator();
        default:

          return new AvgDoubleAggregator();
      }
    }
    //
    else if (CarbonCommonConstants.DISTINCT_COUNT.equalsIgnoreCase(aggregatorType)) {
      switch (dataType) {
        case INT:
        case LONG:
          return new DistinctCountLongAggregatorObjectSet();
        case DECIMAL:
          return new DistinctCountBigDecimalAggregatorObjectSet();
        default:
          return new DistinctCountAggregatorObjectSet();
      }

    } else if (CarbonCommonConstants.SUM.equalsIgnoreCase(aggregatorType)) {
      switch (dataType) {
        case INT:
        case LONG:

          return new SumLongAggregator();
        case DECIMAL:

          return new SumBigDecimalAggregator();

        case TIMESTAMP:

          return new SumTimestampAggregator();
        default:

          return new SumDoubleAggregator();
      }
    } else if (CarbonCommonConstants.SUM_DISTINCT.equalsIgnoreCase(aggregatorType)) {
      switch (dataType) {
        case INT:
        case LONG:

          return new SumDistinctLongAggregator();
        case DECIMAL:

          return new SumDistinctBigDecimalAggregator();
        default:

          return new SumDistinctDoubleAggregator();
      }
    } else if (CarbonCommonConstants.DUMMY.equalsIgnoreCase(aggregatorType)) {
      switch (dataType) {
        case INT:
        case LONG:

          return new DummyLongAggregator();
        case DECIMAL:

          return new DummyBigDecimalAggregator();
        default:

          return new DummyDoubleAggregator();
      }
    } else {
      return null;
    }
  }
}
