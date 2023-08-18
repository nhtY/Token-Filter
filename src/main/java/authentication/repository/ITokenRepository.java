package authentication.repository;

import authentication.records.TokenRecord;

import java.util.Optional;

public interface ITokenRepository {

    TokenRecord saveToken(TokenRecord tokenRecord);

    int deleteToken(TokenRecord tokenRecord);

    Optional<TokenRecord> findToken(String uid);
}
