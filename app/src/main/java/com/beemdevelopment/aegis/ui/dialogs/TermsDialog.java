package com.beemdevelopment.aegis.ui.dialogs;

import android.content.Context;

import com.beemdevelopment.aegis.R;
import com.beemdevelopment.aegis.helpers.MarkdownHtml;

import java.util.Locale;

public class TermsDialog extends SimpleWebViewDialog {
    public TermsDialog() {
        super(R.string.terms_of_use);
    }

    public static TermsDialog create() {
        return new TermsDialog();
    }

    @Override
    protected String getContent(Context context) {
        String asset = "de".equals(Locale.getDefault().getLanguage()) ? "terms.de.md" : "terms.en.md";
        return MarkdownHtml.render(readAssetAsString(context, asset), getBackgroundColor(), getTextColor());
    }
}
