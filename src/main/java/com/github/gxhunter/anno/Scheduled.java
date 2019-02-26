package com.github.gxhunter.anno;



import java.lang.annotation.*;

/**
 * @author 树荫下的天空
 * @date 2019/2/22 17:13
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scheduled {

    /**
     * A cron-like expression, extending the usual UN*X definition to include triggers
     * on the second as well as minute, hour, day of month, month and day of week.
     * <p>E.g. {@code "0 * * * * MON-FRI"} means once per minute on weekdays
     * (at the top of the minute - the 0th second).
     * @return an expression that can be parsed to a cron schedule
     * @see org.springframework.scheduling.support.CronSequenceGenerator
     */
    String cron() default "";

    /**
     * A time zone for which the cron expression will be resolved. By default, this
     * attribute is the empty String (i.e. the server's local time zone will be used).
     * @return a zone id accepted by {@link java.util.TimeZone#getTimeZone(String)},
     * or an empty String to indicate the server's default time zone
     * @since 4.0
     * @see org.springframework.scheduling.support.CronTrigger#CronTrigger(String, java.util.TimeZone)
     * @see java.util.TimeZone
     */
    String zone() default "";

    /**
     * Execute the annotated method with a fixed period in milliseconds between the
     * end of the last invocation and the start of the next.
     * @return the delay in milliseconds
     */
    long fixedDelay() default -1;

    /**
     * Execute the annotated method with a fixed period in milliseconds between the
     * end of the last invocation and the start of the next.
     * @return the delay in milliseconds as a String value, e.g. a placeholder
     * or a {@link java.time.Duration#parse java.time.Duration} compliant value
     * @since 3.2.2
     */
    String fixedDelayString() default "";

    /**
     * Execute the annotated method with a fixed period in milliseconds between
     * invocations.
     * @return the period in milliseconds
     */
    long fixedRate() default -1;

    /**
     * Execute the annotated method with a fixed period in milliseconds between
     * invocations.
     * @return the period in milliseconds as a String value, e.g. a placeholder
     * or a {@link java.time.Duration#parse java.time.Duration} compliant value
     * @since 3.2.2
     */
    String fixedRateString() default "";

    /**
     * Number of milliseconds to delay before the first execution of a
     * {@link #fixedRate()} or {@link #fixedDelay()} task.
     * @return the initial delay in milliseconds
     * @since 3.2
     */
    long initialDelay() default -1;

    /**
     * Number of milliseconds to delay before the first execution of a
     * {@link #fixedRate()} or {@link #fixedDelay()} task.
     * @return the initial delay in milliseconds as a String value, e.g. a placeholder
     * or a {@link java.time.Duration#parse java.time.Duration} compliant value
     * @since 3.2.2
     */
    String initialDelayString() default "";

}
