package semexe.freebase.lexicons.normalizers;

public class IdentityNormalizer implements EntryNormalizer {

    @Override
    public String normalize(String str) {
        return str;
    }

}
