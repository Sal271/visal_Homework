package com.example.loginandregister;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.loginandregister.databinding.FragmentRegisterBinding;
import com.google.android.material.snackbar.Snackbar;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(binding.register, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());

            v.setPadding(systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom);
            return insets;
        });

        binding.registerRootLayout.setOnClickListener(v -> {
            hideKeyboard();
        });

        String fullText = getString(R.string.already_have_an_account_log_in);
        SpannableString spannableString = new SpannableString(fullText);

        // Find the index of "Log in"
        int startIndex = fullText.indexOf("Log in");
        int endIndex = startIndex + "Log in".length();

        // Apply a color to "Log in" if found
        if (startIndex >= 0) {
            int primaryColor = requireContext().getColor(R.color.primary);
            spannableString.setSpan(
                    new ForegroundColorSpan(primaryColor),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        binding.tvLogin.setText(spannableString);
        binding.tvLogin.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        binding.sivGoogle.setOnClickListener(v -> Snackbar.make(binding.getRoot(), "Google Sign-Up Clicked", Snackbar.LENGTH_SHORT).show());
        binding.sivFacebook.setOnClickListener(v -> Snackbar.make(binding.getRoot(), "Facebook Sign-Up Clicked", Snackbar.LENGTH_SHORT).show());
        binding.sivGithub.setOnClickListener(v -> Snackbar.make(binding.getRoot(), "GitHub Sign-Up Clicked", Snackbar.LENGTH_SHORT).show());

        binding.btnRegister.setOnClickListener(v -> {
            hideKeyboard();

            String email = binding.tilEmail.getEditText() != null ? binding.tilEmail.getEditText().getText().toString().trim() : "";
            String username = binding.tilUsername.getEditText() != null ? binding.tilUsername.getEditText().getText().toString().trim() : "";
            String password = binding.tilPass.getEditText() != null ? binding.tilPass.getEditText().getText().toString() : "";
            String confirmPassword = binding.tilConfirmPass.getEditText() != null ? binding.tilConfirmPass.getEditText().getText().toString() : "";

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Please enter a valid email address");
                return;
            } else {
                binding.tilEmail.setError(null);
            }

            if (!password.equals(confirmPassword)) {
                binding.tilConfirmPass.setError("Passwords do not match");
                return;
            } else {
                binding.tilConfirmPass.setError(null);
            }

            String message = "";
            if (binding.tilEmail.getEditText() != null) {
                message += "Email: " + email;
            }
            if (binding.tilUsername.getEditText() != null) {
                message += " Username: " + username;
            }
            if (binding.tilPass.getEditText() != null) {
                message += "\nPassword: " + password;
            }
            if (binding.tilConfirmPass.getEditText() != null) {
                message += " ConfirmPassword: " + confirmPassword;
            }

            Snackbar.make(binding.getRoot(), "Registration Successful!\n" + message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v1 -> {

                    }).show();
        });

        addTextInputListener();
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view == null) {
            view = new View(requireContext());
        }

        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void validateButtonSubmit() {
        boolean isEmailEmpty = binding.tilEmail.getEditText() == null || binding.tilEmail.getEditText().getText().toString().trim().isEmpty();
        boolean isUsernameEmpty = binding.tilUsername.getEditText() == null || binding.tilUsername.getEditText().getText().toString().trim().isEmpty();
        boolean isPasswordEmpty = binding.tilPass.getEditText() == null || binding.tilPass.getEditText().getText().toString().trim().isEmpty();
        boolean isConfirmPassEmpty = binding.tilConfirmPass.getEditText() == null || binding.tilConfirmPass.getEditText().getText().toString().trim().isEmpty();

        if (isEmailEmpty || isUsernameEmpty || isPasswordEmpty || isConfirmPassEmpty) {
            binding.btnRegister.setEnabled(false);
        } else {
            binding.btnRegister.setEnabled(true);
        }
    }

    private void addTextInputListener() {
        TextWatcher handleTextChange = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateButtonSubmit();
            }
        };

        if (binding.tilEmail.getEditText() != null) {
            binding.tilEmail.getEditText().addTextChangedListener(handleTextChange);
        }

        if (binding.tilUsername.getEditText() != null) {
            binding.tilUsername.getEditText().addTextChangedListener(handleTextChange);
        }

        if (binding.tilPass.getEditText() != null) {
            binding.tilPass.getEditText().addTextChangedListener(handleTextChange);
        }

        if (binding.tilConfirmPass.getEditText() != null) {
            binding.tilConfirmPass.getEditText().addTextChangedListener(handleTextChange);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}