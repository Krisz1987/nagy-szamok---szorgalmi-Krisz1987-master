package hu.progmatic.helper;

import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class BadgeExtension implements TestWatcher, AfterAllCallback {
  private static final Pattern methodPointsPattern = Pattern.compile("^.*[^0-9]([0-9]+)(p)$");
  record TestResult(String name, TestResultStatus status, int points) {}
  enum TestResultStatus {
    OK,
    DISABLED,
    FAILED;
  }
  record AutoGradeJson(List<Test> tests) {
    record Test(String name,
        String setup,
        String run,
        String input,
        String output,
        String comparison,
        int timeout,
        int points) {}
  }

  final static List<TestResult> results = new ArrayList<>();

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    var autogradingJson = buildAutogradingJson();
    makeFile(autogradingJson);
  }

  private String buildAutogradingJson() {
    var builder = new GsonBuilder();
    builder.setPrettyPrinting();
    builder.disableHtmlEscaping();
    var gson = builder.create();
    return gson.toJson(new AutoGradeJson(results.stream().map(
        result -> new AutoGradeJson.Test(
            result.name(),
            "",
            result.status() == TestResultStatus.OK ? "/usr/bin/true" : "/usr/bin/false",
            "",
            "",
            "included",
            10,
            result.points()
        )
    ).toList()));
  }

  private void makeFile(String autogradingJson) throws IOException {
    var targetFilePath = Path.of(".github", "classroom", "autograding.json");
    Files.writeString(
        targetFilePath,
        autogradingJson,
        StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING
    );
  }


  @Override
  public void testDisabled(ExtensionContext context, Optional<String> reason) {
    processResult(context, TestResultStatus.DISABLED);
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    processResult(context, TestResultStatus.OK);
  }

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    processResult(context, TestResultStatus.FAILED);
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    processResult(context, TestResultStatus.FAILED);
  }

  private void processResult(ExtensionContext context, TestResultStatus result) {
    var badgeName = getBadgeName(context);
    int points = getPoints(context.getDisplayName());
    results.add(new TestResult(badgeName, result, points));
    var url = getBadgeUrl(context, result);
    saveBadgeTry10(badgeName, url);
  }

  private int getPoints(String badgeName) {
    var matcher = methodPointsPattern.matcher(badgeName);
    var points = 0;
    if (matcher.matches())
      points = Integer.parseInt(matcher.group(1));
    return points;
  }

  private void saveBadgeTry10(String badgeName, String url) {
    int probalkozasok = 0;
    do {
      try {
        saveBadge(badgeName, url);
        return;
      } catch (Exception ignored) {}
    } while (probalkozasok++ < 10);
    System.out.println("Badge frissítése sikertelen. :(");
  }

  private void saveBadge(String badgeName, String url) throws IOException {
    var in = new URL(url).openStream();
    Files.copy(in, Path.of("doc", badgeName + ".svg"), StandardCopyOption.REPLACE_EXISTING);
  }

  private String getBadgeUrl(ExtensionContext context, TestResultStatus result) {
    var name = context.getDisplayName();
    String szoveg;
    String color;
    if (result == TestResultStatus.OK) {
      color = "green";
      szoveg = "OK";
    } else if (result == TestResultStatus.DISABLED) {
      color = "yellow";
      szoveg = "Kihagyott";
    } else {
      color = "red";
      szoveg = "Hibás";
    }
    return String.format(
        "https://badgen.net/badge/%s/%s/%s",
        encodeValue(name),
        encodeValue(szoveg),
        encodeValue(color)
    );
  }

  private String getBadgeName(ExtensionContext context) {
    return String.format(
        "%s_%s",
        context.getTestClass().orElseThrow().getName(),
        context.getTestMethod().orElseThrow().getName()
    );
  }

  private String encodeValue(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
  }
}
