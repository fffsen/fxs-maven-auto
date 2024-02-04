package com.zycm.zybao.common.mvc;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.SqlTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @description: 时间转化成时间戳  原始时间格式为数组形式[2023,5,29]
 * @author: sy
 * @create: 2023-05-29 10:59
 */
public class JsonObjMapper extends ObjectMapper {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public JsonObjMapper() {
        super();
        // 收到未知属性时不报异常
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 统一返回数据的输出风格 转为蛇形命名法
        //this.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        // 反序列化时，属性不存在的兼容处理
        this.getDeserializationConfig()
                .withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 格式化时间
        JavaTimeModule module = new JavaTimeModule();

        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer())
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .addDeserializer(Date.class, new DateDeserializers.DateDeserializer())
                //.addDeserializer(Time.class, new SqlTimeDeserializer())
                //.addDeserializer(java.sql.Date.class, new SqlDateDeserializer())

                // .addSerializer(BigInteger.class, ToStringSerializer.instance)
                // .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(Time.class, new SqlTimeSerializer())
                .addSerializer(java.sql.Date.class, new SqlDateSerializer());

        // 注册功能模块 添加自定义序列化器和反序列化器
        this.registerModule(module);
    }

    /**
     * 序列化实现
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long timestamp = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                gen.writeNumber(timestamp);
            }
        }
    }

    /**
     * 反序列化实现
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            } else {
                //兼容字符串格式时间传入
                String value = p.getValueAsString();
                return LocalDateTimeUtil.of(DateUtil.parse(value));
            }
        }
    }

    /**
     * 反序列 sql类型time
     */
 /*   public static class SqlTimeDeserializer extends JsonDeserializer<Time> {
        @Override
        public Time deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return new Time(timestamp);
            } else {
                //兼容字符串格式时间传入
                String value = p.getValueAsString();
                return new Time(DateUtil.parse(value).getTime());
            }
        }
    }*/

    /**
     * 反序列 sql类型date
     */
   /* public static class SqlDateDeserializer extends JsonDeserializer<java.sql.Date> {
        @Override
        public java.sql.Date deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {
            long timestamp = p.getValueAsLong();
            if (timestamp > 0) {
                return new java.sql.Date(timestamp);
            } else {
                //兼容字符串格式时间传入
                String value = p.getValueAsString();
                return new java.sql.Date(DateUtil.parse(value).getTime());
            }
        }
    }*/

    /**
     * 序列化 sql类型time  用于返回时间数据到展示前端
     */
    public static class SqlTimeSerializer extends JsonSerializer<Time> {
        @Override
        public void serialize(Time value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                //long timestamp = value.getTime();

                gen.writeString(value.toString());//序列化成字符串的时分秒格式
            }
        }
    }
    /**
     * 序列化 sql类型date 用于返回时间数据到展示前端
     */
    public static class SqlDateSerializer extends JsonSerializer<java.sql.Date> {
        @Override
        public void serialize(java.sql.Date value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                //long timestamp = value.getTime();
                gen.writeString(value.toString());
            }
        }
    }
}


