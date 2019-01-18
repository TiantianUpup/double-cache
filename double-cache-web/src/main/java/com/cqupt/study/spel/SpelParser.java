package com.cqupt.study.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.ParameterizedType;

/**
 * @Description: SpEL表达式解析
 *
 * @Author: hetiantian
 * @Date:2019/1/16 18:37 
 * @Version: 1.0
 */
public class SpelParser<T> {
    /**
     * 表达式解析器
     * */
    ExpressionParser parser = new SpelExpressionParser();

    /**
     * 解析SpEL表达式
     *
     * @param spel
     * @param context
     * @return T 解析出来的值
     * */
    public T parse(String spel, EvaluationContext context) {
        Class<T> keyClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T key = parser.parseExpression(spel).getValue(keyClass);
        return key;
    }

    /**
     * 将参数名和参数值存储进EvaluationContext对象中
     *
     * @param object 参数值
     * @param params 参数名
     * @return EvaluationContext对象
     * */
    public EvaluationContext setAndGetContextValue(String[] params, Object[] object) {
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(params[i], object[i]);
        }

        return context;
    }
}
