package com.github.gxhunter.jwt;

/**
 * JWT 加密算法
 * @author hunter
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum AlgEnum{
    /**
     * HS256加密算法
     */
    HS256,
    HS384,
    HS512,
    RS256,
    RS384,
    RS512,
    ES256,
    ES384,
    ES512,
    PS256,
    PS384,
    PS512,
    EdDSA
}
